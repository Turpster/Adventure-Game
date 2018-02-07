package org.ho.yaml;

import java.beans.*;
import org.ho.yaml.exception.*;
import java.util.*;
import java.lang.reflect.*;

public class ReflectionUtil
{
    public static PropertyDescriptor getPropertyDescriptor(final Class clazz, final String s) {
        try {
            for (final PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
                if (s.equals(propertyDescriptor.getName())) {
                    return propertyDescriptor;
                }
            }
        }
        catch (IntrospectionException ex) {}
        return null;
    }
    
    public static boolean hasProperty(final Class clazz, final String s) {
        return null != getPropertyDescriptor(clazz, s);
    }
    
    public static boolean hasProperty(final Object o, final String s) {
        return null != getPropertyDescriptor(o.getClass(), s);
    }
    
    public static PropertyDescriptor getPropertyDescriptor(final Object o, final String s) {
        return getPropertyDescriptor(o.getClass(), s);
    }
    
    public static List<PropertyDescriptor> getProperties(final Object o) {
        return getProperties(o.getClass());
    }
    
    public static List<PropertyDescriptor> getProperties(final Class clazz) {
        try {
            return filterProps(Introspector.getBeanInfo(clazz).getPropertyDescriptors());
        }
        catch (IntrospectionException ex) {
            throw new YamlException(ex);
        }
    }
    
    public static List<PropertyDescriptor> getPropertiesExcluding(final List<String> list, final Object o) {
        try {
            return filterProps(Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors(), list);
        }
        catch (IntrospectionException ex) {
            throw new YamlException(ex);
        }
    }
    
    public static List<Field> getFields(final Object o) {
        final ArrayList<Field> list = new ArrayList<Field>();
        final Field[] fields = o.getClass().getFields();
        for (int length = fields.length, i = 0; i < length; ++i) {
            list.add(fields[i]);
        }
        return list;
    }
    
    public static List<Field> getFieldsExcluding(final List<String> list, final Object o) {
        final ArrayList<Field> list2 = new ArrayList<Field>();
        for (final Field field : o.getClass().getFields()) {
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().equals(field.getName())) {
                    list2.add(field);
                }
            }
        }
        return list2;
    }
    
    public static boolean isMemberField(final Field field) {
        return 0x0 == (0x8 & field.getModifiers());
    }
    
    public static boolean isAbstract(final Class clazz) {
        return 0x0 == (0x0 & clazz.getModifiers());
    }
    
    static List<PropertyDescriptor> filterProps(final PropertyDescriptor[] array) {
        return filterProps(array, null);
    }
    
    static List<PropertyDescriptor> filterProps(final PropertyDescriptor[] array, final List<String> list) {
        final ArrayList<PropertyDescriptor> list2 = new ArrayList<PropertyDescriptor>();
        for (final PropertyDescriptor propertyDescriptor : array) {
            if (list != null) {
                if (!"class".equals(propertyDescriptor.getName())) {
                    final Iterator<String> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        if (!iterator.next().equals(propertyDescriptor.getName())) {
                            list2.add(propertyDescriptor);
                        }
                    }
                }
            }
            else if (propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null) {
                list2.add(propertyDescriptor);
            }
        }
        return list2;
    }
    
    public static void setProperty(final Object o, final String s, final Object o2) throws IllegalAccessException, InvocationTargetException {
        getPropertyDescriptor(o, s).getWriteMethod().invoke(o, o2);
    }
    
    public static Class getFieldType(final Class clazz, final String s) {
        try {
            return clazz.getField(s).getType();
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static Object readField(final Object o, final String s) {
        try {
            final Field field = o.getClass().getField(s);
            field.setAccessible(true);
            return field.get(o);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static void setField(final Object o, final String s, final Object o2) {
        try {
            final Field field = o.getClass().getField(s);
            field.setAccessible(true);
            field.set(o, o2);
        }
        catch (Exception ex) {}
    }
    
    public static Object invokeConstructor(final Class clazz, final Class[] array, final Object[] array2) {
        final Constructor constructor = getConstructor(clazz, array);
        try {
            return constructor.newInstance(array2);
        }
        catch (Exception ex) {
            throw new YamlException("Can't invoke constructor for " + clazz + " with arguments " + Arrays.asList((Class[])array) + " with values " + Arrays.asList(array2));
        }
    }
    
    public static Constructor getConstructor(final Class clazz, final Class[] array) {
        try {
            return clazz.getConstructor((Class[])array);
        }
        catch (Exception ex) {
            throw new YamlException("Can't find constructor for " + clazz + " with arguments " + Arrays.asList((Class[])array) + "\n" + ex);
        }
    }
    
    public static boolean isPrimitiveType(final String s) {
        return Integer.TYPE.getName().equals(s) || Double.TYPE.getName().equals(s) || Float.TYPE.getName().equals(s) || Boolean.TYPE.getName().equals(s) || Character.TYPE.getName().equals(s) || Byte.TYPE.getName().equals(s) || Long.TYPE.getName().equals(s) || Short.TYPE.getName().equals(s) || Character.TYPE.getName().equals(s);
    }
    
    public static Class getPrimitiveType(final String s) {
        if (Integer.TYPE.getName().equals(s)) {
            return Integer.TYPE;
        }
        if (Double.TYPE.getName().equals(s)) {
            return Double.TYPE;
        }
        if (Float.TYPE.getName().equals(s)) {
            return Float.TYPE;
        }
        if (Boolean.TYPE.getName().equals(s)) {
            return Boolean.TYPE;
        }
        if (Character.TYPE.getName().equals(s)) {
            return Character.TYPE;
        }
        if (Byte.TYPE.getName().equals(s)) {
            return Byte.TYPE;
        }
        if (Long.TYPE.getName().equals(s)) {
            return Long.TYPE;
        }
        if (Short.TYPE.getName().equals(s)) {
            return Short.TYPE;
        }
        if (Character.TYPE.getName().equals(s)) {
            return Character.TYPE;
        }
        throw new YamlException(s + " is not a primitive type.");
    }
    
    public static boolean isArrayName(final String s) {
        return s != null && s.indexOf("[]") != -1;
    }
    
    public static String arrayComponentName(final String s) {
        return s.substring(0, s.length() - 2);
    }
    
    public static Class getArrayType(final String s) {
        if (isArrayName(s)) {
            return getArrayTypeHelper(s);
        }
        return null;
    }
    
    public static String arrayName(final Class clazz, final YamlConfig yamlConfig) {
        if (clazz.isArray()) {
            return arrayName(clazz.getComponentType(), yamlConfig) + "[]";
        }
        if (yamlConfig != null) {
            return yamlConfig.classname2transfer(clazz.getName());
        }
        return clazz.getName();
    }
    
    public static Class classForName(final String s) {
        if (isPrimitiveType(s)) {
            return getPrimitiveType(s);
        }
        if (isArrayName(s)) {
            return getArrayType(s);
        }
        try {
            return Class.forName(s);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public static String transfer2classname(final String s, final YamlConfig yamlConfig) {
        if (isArrayName(s)) {
            return transfer2classname(arrayComponentName(s), yamlConfig) + "[]";
        }
        return yamlConfig.transfer2classname(s);
    }
    
    static Class getArrayTypeHelper(final String s) {
        if (!isArrayName(s)) {
            return classForName(s);
        }
        return Array.newInstance(getArrayTypeHelper(arrayComponentName(s)), 0).getClass();
    }
    
    public static String className(final Class clazz) {
        return className(clazz, null);
    }
    
    public static String className(final Class clazz, final YamlConfig yamlConfig) {
        if (clazz.isArray()) {
            return arrayName(clazz, yamlConfig);
        }
        if (yamlConfig != null) {
            return yamlConfig.classname2transfer(clazz.getName());
        }
        return clazz.getName();
    }
    
    public static boolean isSimpleType(final Class clazz) {
        return clazz.isPrimitive() || clazz == String.class || clazz == Integer.class || clazz == Long.class || clazz == Short.class || clazz == Double.class || clazz == Float.class || clazz == Boolean.class || clazz == Character.class;
    }
    
    public static boolean isCastableFrom(final Class clazz, final Class clazz2) {
        if (clazz2.isAssignableFrom(clazz)) {
            return true;
        }
        if (clazz2.isPrimitive()) {
            try {
                if (clazz.getField("TYPE").get(clazz) == clazz2) {
                    return true;
                }
            }
            catch (Exception ex) {}
        }
        return false;
    }
    
    public static boolean isCastableFrom(final Class clazz, final String s) {
        return isCastableFrom(clazz, classForName(s));
    }
}
