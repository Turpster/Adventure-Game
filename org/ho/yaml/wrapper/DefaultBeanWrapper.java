package org.ho.yaml.wrapper;

import org.ho.yaml.exception.*;
import java.beans.*;
import java.lang.reflect.*;
import org.ho.yaml.*;
import java.util.*;

public class DefaultBeanWrapper extends AbstractWrapper implements MapWrapper
{
    public DefaultBeanWrapper(final Class clazz) {
        super(clazz);
    }
    
    public boolean hasProperty(final String s) {
        return this.config.isPropertyAccessibleForEncoding(ReflectionUtil.getPropertyDescriptor(this.type, s));
    }
    
    public Object getProperty(final String s) throws PropertyAccessException {
        return this.getProperty(this.getObject(), s);
    }
    
    public Object getProperty(final Object o, final String s) {
        try {
            final PropertyDescriptor propertyDescriptor = ReflectionUtil.getPropertyDescriptor(this.type, s);
            if (this.config.isPropertyAccessibleForEncoding(propertyDescriptor)) {
                final Method readMethod = propertyDescriptor.getReadMethod();
                readMethod.setAccessible(true);
                return readMethod.invoke(o, (Object[])null);
            }
        }
        catch (Exception ex) {}
        try {
            final Field declaredField = this.type.getDeclaredField(s);
            if (this.config.isFieldAccessibleForEncoding(declaredField)) {
                declaredField.setAccessible(true);
                return declaredField.get(o);
            }
        }
        catch (Exception ex2) {}
        throw new PropertyAccessException("Can't get " + s + " property on type " + this.type + ".");
    }
    
    public void setProperty(final String s, final Object o) throws PropertyAccessException {
        try {
            final PropertyDescriptor propertyDescriptor = ReflectionUtil.getPropertyDescriptor(this.type, s);
            if (this.config.isPropertyAccessibleForEncoding(propertyDescriptor)) {
                final Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.setAccessible(true);
                writeMethod.invoke(this.getObject(), o);
                return;
            }
        }
        catch (Exception ex) {}
        try {
            final Field declaredField = this.type.getDeclaredField(s);
            if (this.config.isFieldAccessibleForDecoding(declaredField)) {
                declaredField.setAccessible(true);
                declaredField.set(this.getObject(), o);
            }
        }
        catch (Exception ex2) {}
    }
    
    public Class getPropertyType(final String s) {
        if (ReflectionUtil.hasProperty(this.type, s)) {
            return ReflectionUtil.getPropertyDescriptor(this.type, s).getPropertyType();
        }
        try {
            final Field declaredField = this.type.getDeclaredField(s);
            if (ReflectionUtil.isMemberField(declaredField)) {
                declaredField.setAccessible(true);
                return declaredField.getType();
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    public boolean containsKey(final Object o) {
        return this.hasProperty((String)o);
    }
    
    public Object get(final Object o) {
        return this.getProperty((String)o);
    }
    
    public Class getExpectedType(final Object o) {
        return this.getPropertyType((String)o);
    }
    
    public Collection keys() {
        final Object prototype = this.createPrototype();
        final HashSet<String> set = new HashSet<String>();
        for (final PropertyDescriptor propertyDescriptor : ReflectionUtil.getProperties(this.getType())) {
            if (this.config.isPropertyAccessibleForEncoding(propertyDescriptor)) {
                try {
                    if (Utilities.same(this.getProperty(this.getObject(), propertyDescriptor.getName()), this.getProperty(prototype, propertyDescriptor.getName()))) {
                        continue;
                    }
                    set.add(propertyDescriptor.getName());
                }
                catch (Exception ex) {}
            }
        }
        for (final Field field : this.getType().getDeclaredFields()) {
            if (this.config.isFieldAccessibleForEncoding(field)) {
                field.setAccessible(true);
                try {
                    if (!Utilities.same(field.get(prototype), field.get(this.getObject()))) {
                        set.add(field.getName());
                    }
                }
                catch (Exception ex2) {}
            }
        }
        final ArrayList list = new ArrayList<Object>(set);
        Collections.sort((List<E>)list, (Comparator<? super E>)new Comparator<String>() {
            public int compare(final String s, final String s2) {
                return s.compareTo(s2);
            }
        });
        return list;
    }
    
    public void put(final Object o, final Object o2) {
        this.setProperty((String)o, o2);
    }
}
