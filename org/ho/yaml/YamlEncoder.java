package org.ho.yaml;

import org.ho.yaml.exception.*;
import java.io.*;
import org.ho.yaml.wrapper.*;
import java.util.*;

public class YamlEncoder
{
    PrintWriter out;
    Map<Object, ObjectEntry> referenceMap;
    YamlConfig config;
    int nextRefName;
    
    public YamlEncoder(final OutputStream outputStream) {
        this.referenceMap = new IdentityHashMap<Object, ObjectEntry>();
        this.config = YamlConfig.getDefaultConfig();
        this.nextRefName = 0;
        try {
            this.out = new PrintWriter(new OutputStreamWriter(outputStream, this.config.encoding));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.config.encoding);
        }
    }
    
    public YamlEncoder(final OutputStream outputStream, final YamlConfig config) {
        this.referenceMap = new IdentityHashMap<Object, ObjectEntry>();
        this.config = YamlConfig.getDefaultConfig();
        this.nextRefName = 0;
        try {
            this.config = config;
            this.out = new PrintWriter(new OutputStreamWriter(outputStream, config.encoding));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + config.encoding);
        }
    }
    
    public String getIndentAmount() {
        return this.config.getIndentAmount();
    }
    
    public void setIndentAmount(final String indentAmount) {
        this.config.setIndentAmount(indentAmount);
    }
    
    public boolean isMinimalOutput() {
        return this.config.isMinimalOutput();
    }
    
    public void setMinimalOutput(final boolean minimalOutput) {
        this.config.setMinimalOutput(minimalOutput);
    }
    
    void traverseAndCount(final Object o) {
        if (o == null) {
            return;
        }
        if (o instanceof String) {
            return;
        }
        this.mark(o);
        if (this.refCount(o) > 1) {
            return;
        }
        if (ReflectionUtil.isSimpleType(o.getClass())) {
            return;
        }
        final ObjectWrapper wrapper = this.getConfig().getWrapper(o);
        if (wrapper instanceof CollectionWrapper) {
            this.traverseAndCountCollection((CollectionWrapper)wrapper);
        }
        else if (wrapper instanceof MapWrapper) {
            this.traverseAndCountMap((MapWrapper)wrapper);
        }
    }
    
    void traverseAndCountCollection(final CollectionWrapper collectionWrapper) {
        final Iterator<Object> iterator = collectionWrapper.iterator();
        while (iterator.hasNext()) {
            this.traverseAndCount(iterator.next());
        }
    }
    
    void traverseAndCountMap(final MapWrapper mapWrapper) {
        for (final Object next : mapWrapper.keys()) {
            final Object value = mapWrapper.get(next);
            this.traverseAndCount(next);
            this.traverseAndCount(value);
        }
    }
    
    int refCount(final Object o) {
        final ObjectEntry objectEntry = this.referenceMap.get(o);
        return (objectEntry != null) ? objectEntry.refs : 0;
    }
    
    boolean toBeAnchored(final Object o) {
        final ObjectEntry objectEntry = this.referenceMap.get(o);
        return objectEntry != null && objectEntry.refs > 1 && !objectEntry.anchorDeclared;
    }
    
    boolean toBeAliased(final Object o) {
        final ObjectEntry objectEntry = this.referenceMap.get(o);
        return objectEntry != null && objectEntry.refs > 1 && objectEntry.anchorDeclared;
    }
    
    void mark(final Object o) {
        ObjectEntry objectEntry = this.referenceMap.get(o);
        if (objectEntry == null) {
            objectEntry = new ObjectEntry(o);
            this.referenceMap.put(o, objectEntry);
            objectEntry.ref = this.nextRefName++;
        }
        final ObjectEntry objectEntry2 = objectEntry;
        ++objectEntry2.refs;
    }
    
    public void writeObject(final Object o) {
        this.traverseAndCount(o);
        this.out.print("--- ");
        this.writeObject(o, "", o.getClass());
        this.reset();
    }
    
    void reset() {
        this.referenceMap.clear();
    }
    
    String indent(final String s) {
        return this.getIndentAmount() + s;
    }
    
    void writeObject(final Object o, final String s, final Class clazz) {
        if (o == null) {
            this.out.println("~");
        }
        else if (this.toBeAliased(o)) {
            this.writeReference(o);
        }
        else {
            if (this.toBeAnchored(o)) {
                this.writeAlias(o);
            }
            final ObjectWrapper wrapper = this.getConfig().getWrapper(o);
            if (wrapper instanceof SimpleObjectWrapper) {
                this.writeSimpleValue((SimpleObjectWrapper)wrapper, clazz, s);
            }
            else if (wrapper instanceof CollectionWrapper) {
                this.writeCollection((CollectionWrapper)wrapper, clazz, s);
            }
            else if (wrapper instanceof MapWrapper) {
                this.writeMap((MapWrapper)wrapper, s, clazz);
            }
        }
    }
    
    void writeReference(final Object o) {
        this.out.println("*" + this.referenceMap.get(o).ref);
    }
    
    void writeAlias(final Object o) {
        final ObjectEntry objectEntry = this.referenceMap.get(o);
        this.out.print("&" + objectEntry.ref + " ");
        objectEntry.anchorDeclared = true;
    }
    
    void writeSimpleValue(final SimpleObjectWrapper simpleObjectWrapper, final Class clazz, final String s) {
        if ((!Utilities.classEquals(clazz, simpleObjectWrapper.getType()) || !this.isMinimalOutput()) && simpleObjectWrapper.getType() != Integer.class && simpleObjectWrapper.getType() != Boolean.class && simpleObjectWrapper.getType() != String.class) {
            this.out.print("!" + this.getTransferName(simpleObjectWrapper.getType()) + " ");
        }
        final Object outputValue = simpleObjectWrapper.getOutputValue();
        if (outputValue == null) {
            this.out.println("~");
        }
        else if (outputValue instanceof String || outputValue instanceof Character) {
            this.out.println(Utilities.stringify(outputValue, s));
        }
        else {
            this.out.println(outputValue);
        }
    }
    
    void writeMap(final MapWrapper mapWrapper, final String s, final Class clazz) {
        if ((this.isMinimalOutput() && clazz == mapWrapper.getType()) || clazz == HashMap.class || clazz == Map.class) {
            this.out.print("");
        }
        else {
            this.out.print("!" + this.getTransferName(mapWrapper.getType()));
        }
        if (mapWrapper.keys().size() == 0) {
            this.out.println(" {}");
        }
        else {
            this.out.println("");
            for (final Object next : mapWrapper.keys()) {
                final Object value = mapWrapper.get(next);
                this.out.print(s + Utilities.stringify(next) + ": ");
                this.writeObject(value, this.indent(s), mapWrapper.getExpectedType(next));
            }
        }
    }
    
    void writeCollection(final CollectionWrapper collectionWrapper, final Class clazz, final String s) {
        if (collectionWrapper.size() > 0) {
            if ((this.isMinimalOutput() && clazz == collectionWrapper.getType()) || collectionWrapper.getType() == ArrayList.class || clazz == List.class) {
                this.out.println();
            }
            else {
                this.out.println("!" + this.getTransferName(collectionWrapper.getType()));
            }
            for (final Object next : collectionWrapper) {
                this.out.print(s + "- ");
                this.writeObject(next, this.indent(s), collectionWrapper.isTyped() ? collectionWrapper.componentType() : null);
            }
        }
        else {
            if ((this.isMinimalOutput() && clazz == collectionWrapper.getType()) || collectionWrapper.getType() == ArrayList.class || clazz == List.class) {
                this.out.print("");
            }
            else {
                this.out.print("!" + this.getTransferName(collectionWrapper.getType()) + " ");
            }
            if (collectionWrapper.size() == 0) {
                this.out.println("[]");
            }
        }
    }
    
    String getTransferName(final Class clazz) {
        return ReflectionUtil.className(clazz, this.config);
    }
    
    public void close() {
        this.out.close();
    }
    
    public void flush() {
        this.out.flush();
    }
    
    public YamlConfig getConfig() {
        return this.config;
    }
    
    public void setConfig(final YamlConfig config) {
        this.config = config;
    }
    
    class ObjectEntry
    {
        Object target;
        int ref;
        int refs;
        boolean anchorDeclared;
        
        ObjectEntry(final Object target) {
            this.refs = 0;
            this.anchorDeclared = false;
            this.target = target;
        }
        
        public String toString() {
            return "{target: " + this.target + ", refname: " + this.ref + ", refs: " + this.refs + "}";
        }
    }
}
