package org.ho.yaml;

import java.text.*;
import java.math.*;
import org.ho.yaml.exception.*;
import org.ho.yaml.wrapper.*;
import org.ho.util.*;
import java.util.*;
import java.io.*;
import java.beans.*;
import java.lang.reflect.*;

public class YamlConfig implements YamlOperations, Cloneable
{
    private static YamlConfig defaultConfig;
    public static final String CONSTRUCTOR_SCOPE = "constructor";
    public static final String FIELD_SCOPE = "field";
    public static final String PROPERTY_SCOPE = "property";
    public static final String PRIVATE = "private";
    public static final String DEFAULT = "default";
    public static final String PROTECTED = "protected";
    public static final String PUBLIC = "public";
    String indentAmount;
    boolean minimalOutput;
    boolean suppressWarnings;
    BiDirectionalMap<String, String> transfers;
    private String dateFormat;
    private DateFormat dateFormatter;
    Map<String, String> decodingAccessScope;
    Map<String, String> encodingAccessScope;
    Map<String, Object> handlers;
    String encoding;
    
    public YamlConfig() {
        this.indentAmount = "  ";
        this.minimalOutput = false;
        this.suppressWarnings = false;
        this.transfers = null;
        this.dateFormat = null;
        this.dateFormatter = null;
        this.decodingAccessScope = new HashMap<String, String>();
        this.encodingAccessScope = new HashMap<String, String>();
        this.handlers = new HashMap<String, Object>();
        this.encoding = "UTF-8";
        this.installDefaultHandlers();
        this.installDefaultAccessScopes();
    }
    
    public static YamlConfig getDefaultConfig() {
        return (YamlConfig.defaultConfig == null) ? null : ((YamlConfig)YamlConfig.defaultConfig.clone());
    }
    
    public String getEncoding() {
        return this.encoding;
    }
    
    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
    
    public String getIndentAmount() {
        return this.indentAmount;
    }
    
    public void setIndentAmount(final String indentAmount) {
        this.indentAmount = indentAmount;
    }
    
    public boolean isMinimalOutput() {
        return this.minimalOutput;
    }
    
    public void setMinimalOutput(final boolean minimalOutput) {
        this.minimalOutput = minimalOutput;
    }
    
    public boolean isSuppressWarnings() {
        return this.suppressWarnings;
    }
    
    public void setSuppressWarnings(final boolean suppressWarnings) {
        this.suppressWarnings = suppressWarnings;
    }
    
    public BiDirectionalMap<String, String> getTransfers() {
        return this.transfers;
    }
    
    public void setTransfers(final BiDirectionalMap<String, String> transfers) {
        this.transfers = transfers;
    }
    
    public Map<String, Object> getHandlers() {
        return this.handlers;
    }
    
    public void setHandlers(final Map<String, Object> handlers) {
        this.handlers = handlers;
        this.installDefaultHandlers();
    }
    
    void installDefaultHandlers() {
        this.install("java.awt.Dimension", DimensionWrapper.class.getName());
        this.install("java.awt.Point", PointWrapper.class.getName());
        this.install("java.awt.Color", ColorWrapper.class.getName());
        this.install("java.lang.Class", ClassWrapper.class.getName());
        this.install("java.math.BigInteger", new OneArgConstructorTypeWrapper(BigInteger.class, String.class.getName()));
        this.install("java.math.BigDecimal", new OneArgConstructorTypeWrapper(BigDecimal.class, String.class.getName()));
        this.install(File.class.getName(), new OneArgConstructorTypeWrapper(File.class, String.class.getName()));
        this.install(Date.class.getName(), new DateWrapper());
    }
    
    void installDefaultAccessScopes() {
        this.decodingAccessScope.put("field", "public");
        this.decodingAccessScope.put("property", "public");
        this.decodingAccessScope.put("constructor", "public");
        this.encodingAccessScope.put("field", "public");
        this.encodingAccessScope.put("property", "public");
    }
    
    void install(final String s, final Object o) {
        if (o instanceof ObjectWrapper) {
            ((ObjectWrapper)o).setYamlConfig(this);
        }
        if (!this.handlers.containsKey(s)) {
            this.handlers.put(s, o);
        }
    }
    
    String transfer2classname(final String s) {
        if (this.transfers != null && this.transfers.containsKey(s)) {
            return this.transfers.get(s);
        }
        return s;
    }
    
    String classname2transfer(final String s) {
        if (this.transfers != null && this.transfers.getReverse().containsKey(s)) {
            return this.transfers.getReverse().get(s);
        }
        return s;
    }
    
    public ObjectWrapper getWrapper(final Object object) {
        final ObjectWrapper wrapper = this.getWrapper(object.getClass());
        wrapper.setObject(object);
        return wrapper;
    }
    
    public ObjectWrapper getWrapper(final Class clazz) {
        return this.getWrapper(ReflectionUtil.className(clazz));
    }
    
    ObjectWrapper initWrapper(final String s, final Class clazz) {
        final WrapperFactory value = this.handlers.get(s);
        if (value instanceof String) {
            try {
                return ReflectionUtil.classForName((String)value).getConstructor(Class.class).newInstance(clazz);
            }
            catch (Exception ex) {
                throw new YamlException("Error initializing Wrapper " + value + " for type " + clazz, ex);
            }
        }
        if (value instanceof WrapperFactory) {
            return value.makeWrapper();
        }
        return null;
    }
    
    public ObjectWrapper getWrapper(final String s) {
        final Class classForName = ReflectionUtil.classForName(this.transfer2classname(s));
        if (classForName == null) {
            return null;
        }
        ObjectWrapper initWrapper;
        if (this.handlers != null && this.handlers.containsKey(s)) {
            initWrapper = this.initWrapper(s, classForName);
        }
        else if (Map.class.isAssignableFrom(classForName)) {
            initWrapper = new DefaultMapWrapper(classForName);
        }
        else if (Collection.class.isAssignableFrom(classForName)) {
            initWrapper = new DefaultCollectionWrapper(classForName);
        }
        else if (classForName.isArray()) {
            initWrapper = new ArrayWrapper(classForName);
        }
        else {
            if (ReflectionUtil.isSimpleType(classForName)) {
                return new DefaultSimpleTypeWrapper(classForName);
            }
            if (classForName.isEnum()) {
                initWrapper = new EnumWrapper(classForName);
            }
            else {
                initWrapper = new DefaultBeanWrapper(classForName);
            }
        }
        initWrapper.setYamlConfig(this);
        return initWrapper;
    }
    
    public ObjectWrapper getWrapperSetContent(final String s, final String s2) {
        final Class classForName = ReflectionUtil.classForName(this.transfer2classname(s));
        SimpleObjectWrapper simpleObjectWrapper;
        if (this.handlers != null && this.handlers.containsKey(s)) {
            simpleObjectWrapper = (SimpleObjectWrapper)this.initWrapper(s, classForName);
            simpleObjectWrapper.setObject(Utilities.convertType(s2, simpleObjectWrapper.expectedArgType()));
        }
        else if (classForName != null && classForName.isEnum()) {
            simpleObjectWrapper = new EnumWrapper(classForName);
            simpleObjectWrapper.setObject(Utilities.convertType(s2, simpleObjectWrapper.expectedArgType()));
        }
        else {
            simpleObjectWrapper = new DefaultSimpleTypeWrapper(classForName);
            simpleObjectWrapper.setObject(Utilities.convertType(s2, simpleObjectWrapper.expectedArgType()));
        }
        simpleObjectWrapper.setYamlConfig(this);
        return simpleObjectWrapper;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new Error();
        }
    }
    
    public String getDateFormat() {
        return this.dateFormat;
    }
    
    public void setDateFormat(final String dateFormat) {
        this.dateFormat = dateFormat;
        this.dateFormatter = null;
    }
    
    public DateFormat getDateFormatter() {
        if (this.dateFormatter == null && this.dateFormat != null) {
            this.dateFormatter = new DateTimeParser(this.dateFormat);
        }
        return this.dateFormatter;
    }
    
    public static YamlConfig fromFile(final String s) throws FileNotFoundException, EOFException {
        final YamlDecoder yamlDecoder = new YamlDecoder(new FileInputStream(s), new YamlConfig());
        final YamlConfig yamlConfig = yamlDecoder.readObjectOfType(YamlConfig.class);
        yamlDecoder.close();
        return yamlConfig;
    }
    
    public static YamlConfig fromResource(final String s) throws EOFException {
        final YamlDecoder yamlDecoder = new YamlDecoder(YamlConfig.class.getClassLoader().getResourceAsStream(s), new YamlConfig());
        final YamlConfig yamlConfig = yamlDecoder.readObjectOfType(YamlConfig.class);
        yamlDecoder.close();
        return yamlConfig;
    }
    
    public Object load(final YamlDecoder yamlDecoder) {
        yamlDecoder.setConfig(this);
        Object object = null;
        try {
            object = yamlDecoder.readObject();
        }
        catch (EOFException ex) {}
        return object;
    }
    
    public Object load(final InputStream inputStream) {
        return this.load(new YamlDecoder(inputStream, this));
    }
    
    public Object load(final Reader reader) {
        return this.load(new YamlDecoder(reader, this));
    }
    
    public Object load(final File file) throws FileNotFoundException {
        return this.load(new YamlDecoder(new FileInputStream(file), this));
    }
    
    public Object load(final String s) {
        try {
            return this.load(new ByteArrayInputStream(s.getBytes(this.getEncoding())));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public <T> T loadType(final YamlDecoder yamlDecoder, final Class<T> clazz) {
        yamlDecoder.setConfig(this);
        T objectOfType = null;
        try {
            objectOfType = yamlDecoder.readObjectOfType(clazz);
        }
        catch (EOFException ex) {}
        return objectOfType;
    }
    
    public <T> T loadType(final InputStream inputStream, final Class<T> clazz) {
        return this.loadType(new YamlDecoder(inputStream, this), clazz);
    }
    
    public <T> T loadType(final Reader reader, final Class<T> clazz) {
        return this.loadType(new YamlDecoder(reader, this), clazz);
    }
    
    public <T> T loadType(final File file, final Class<T> clazz) throws FileNotFoundException {
        return this.loadType(new YamlDecoder(new FileInputStream(file), this), clazz);
    }
    
    public <T> T loadType(final String s, final Class<T> clazz) {
        try {
            return this.loadType(new ByteArrayInputStream(s.getBytes(this.getEncoding())), clazz);
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public YamlStream loadStream(final YamlDecoder yamlDecoder) {
        yamlDecoder.setConfig(this);
        return yamlDecoder.asStream();
    }
    
    public YamlStream loadStream(final Reader reader) {
        return this.loadStream(new YamlDecoder(reader, this));
    }
    
    public YamlStream loadStream(final InputStream inputStream) {
        return this.loadStream(new YamlDecoder(inputStream, this));
    }
    
    public YamlStream loadStream(final File file) throws FileNotFoundException {
        return this.loadStream(new YamlDecoder(new FileInputStream(file), this));
    }
    
    public YamlStream loadStream(final String s) {
        try {
            return this.loadStream(new ByteArrayInputStream(s.getBytes(this.getEncoding())));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public <T> YamlStream<T> loadStreamOfType(final YamlDecoder yamlDecoder, final Class<T> clazz) {
        yamlDecoder.setConfig(this);
        return (YamlStream<T>)yamlDecoder.asStreamOfType(clazz);
    }
    
    public <T> YamlStream<T> loadStreamOfType(final Reader reader, final Class<T> clazz) {
        return this.loadStreamOfType(new YamlDecoder(reader, this), clazz);
    }
    
    public <T> YamlStream<T> loadStreamOfType(final InputStream inputStream, final Class<T> clazz) {
        return this.loadStreamOfType(new YamlDecoder(inputStream, this), clazz);
    }
    
    public <T> YamlStream<T> loadStreamOfType(final File file, final Class<T> clazz) throws FileNotFoundException {
        return this.loadStreamOfType(new YamlDecoder(new FileInputStream(file), this), clazz);
    }
    
    public <T> YamlStream<T> loadStreamOfType(final String s, final Class<T> clazz) {
        try {
            return this.loadStreamOfType(new ByteArrayInputStream(s.getBytes(this.getEncoding())), clazz);
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public void dump(final Object o, final File file) throws FileNotFoundException {
        final YamlEncoder yamlEncoder = new YamlEncoder(new FileOutputStream(file), this);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
    }
    
    public void dump(final Object o, final File file, final boolean minimalOutput) throws FileNotFoundException {
        final YamlEncoder yamlEncoder = new YamlEncoder(new FileOutputStream(file), (YamlConfig)this.clone());
        yamlEncoder.setMinimalOutput(minimalOutput);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
    }
    
    public void dumpStream(final Iterator iterator, final File file, final boolean minimalOutput) throws FileNotFoundException {
        final YamlEncoder yamlEncoder = new YamlEncoder(new FileOutputStream(file), (YamlConfig)this.clone());
        yamlEncoder.setMinimalOutput(minimalOutput);
        while (iterator.hasNext()) {
            yamlEncoder.writeObject(iterator.next());
        }
        yamlEncoder.close();
    }
    
    public void dumpStream(final Iterator iterator, final File file) throws FileNotFoundException {
        final YamlEncoder yamlEncoder = new YamlEncoder(new FileOutputStream(file), this);
        while (iterator.hasNext()) {
            yamlEncoder.writeObject(iterator.next());
        }
        yamlEncoder.close();
    }
    
    public String dump(final Object o) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final YamlEncoder yamlEncoder = new YamlEncoder(byteArrayOutputStream, this);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
        try {
            return new String(byteArrayOutputStream.toByteArray(), this.getEncoding());
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public String dump(final Object o, final boolean minimalOutput) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final YamlEncoder yamlEncoder = new YamlEncoder(byteArrayOutputStream, (YamlConfig)this.clone());
        yamlEncoder.setMinimalOutput(minimalOutput);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
        try {
            return new String(byteArrayOutputStream.toByteArray(), this.getEncoding());
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public String dumpStream(final Iterator iterator) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final YamlEncoder yamlEncoder = new YamlEncoder(byteArrayOutputStream, this);
        while (iterator.hasNext()) {
            yamlEncoder.writeObject(iterator.next());
        }
        yamlEncoder.close();
        try {
            return new String(byteArrayOutputStream.toByteArray(), this.getEncoding());
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public String dumpStream(final Iterator iterator, final boolean minimalOutput) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final YamlEncoder yamlEncoder = new YamlEncoder(byteArrayOutputStream, (YamlConfig)this.clone());
        yamlEncoder.setMinimalOutput(minimalOutput);
        while (iterator.hasNext()) {
            yamlEncoder.writeObject(iterator.next());
        }
        yamlEncoder.close();
        try {
            return new String(byteArrayOutputStream.toByteArray(), this.getEncoding());
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.getEncoding());
        }
    }
    
    public Map<String, String> getDecodingAccessScope() {
        return this.decodingAccessScope;
    }
    
    public void setDecodingAccessScope(final Map<String, String> decodingAccessScope) {
        this.decodingAccessScope = decodingAccessScope;
    }
    
    public Map<String, String> getEncodingAccessScope() {
        return this.encodingAccessScope;
    }
    
    public void setEncodingAccessScope(final Map<String, String> encodingAccessScope) {
        this.encodingAccessScope = encodingAccessScope;
    }
    
    public boolean isFieldAccessibleForDecoding(final Field field) {
        return ReflectionUtil.isMemberField(field) && this.isWithin(field.getModifiers(), this.decodingAccessScope.get("field"));
    }
    
    public boolean isFieldAccessibleForEncoding(final Field field) {
        return ReflectionUtil.isMemberField(field) && this.isWithin(field.getModifiers(), this.encodingAccessScope.get("field"));
    }
    
    public boolean isConstructorAccessibleForDecoding(final Class clazz) {
        try {
            return this.isWithin(clazz.getDeclaredConstructor((Class[])null).getModifiers(), this.decodingAccessScope.get("constructor"));
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    public boolean isPropertyAccessibleForDecoding(final PropertyDescriptor propertyDescriptor) {
        return this.isWithin(propertyDescriptor.getWriteMethod().getModifiers(), this.decodingAccessScope.get("property"));
    }
    
    public boolean isPropertyAccessibleForEncoding(final PropertyDescriptor propertyDescriptor) {
        return this.isWithin(propertyDescriptor.getReadMethod().getModifiers(), this.encodingAccessScope.get("property"));
    }
    
    boolean isWithin(final int n, final String s) {
        final boolean public1 = Modifier.isPublic(n);
        final boolean private1 = Modifier.isPrivate(n);
        final boolean protected1 = Modifier.isProtected(n);
        final boolean b = !public1 & !private1 & !protected1;
        if ("public".equals(s)) {
            return public1;
        }
        if ("protected".equals(s)) {
            return public1 || protected1;
        }
        return !"default".equals(s) || b || public1 || protected1;
    }
    
    public void dump(final Object o, final OutputStream outputStream, final boolean minimalOutput) {
        final YamlEncoder yamlEncoder = new YamlEncoder(outputStream, (YamlConfig)this.clone());
        yamlEncoder.setMinimalOutput(minimalOutput);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
    }
    
    public void dump(final Object o, final OutputStream outputStream) {
        final YamlEncoder yamlEncoder = new YamlEncoder(outputStream, this);
        yamlEncoder.writeObject(o);
        yamlEncoder.close();
    }
    
    static {
        try {
            YamlConfig.defaultConfig = fromResource("jyaml.yml");
        }
        catch (Exception ex) {
            try {
                YamlConfig.defaultConfig = fromFile("jyaml.yml");
            }
            catch (Exception ex2) {
                YamlConfig.defaultConfig = new YamlConfig();
            }
        }
    }
}
