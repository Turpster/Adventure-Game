package org.ho.yaml.wrapper;

import org.ho.yaml.*;
import org.ho.yaml.exception.*;
import java.lang.reflect.*;
import java.util.*;

public class ArrayWrapper extends AbstractWrapper implements CollectionWrapper
{
    ArrayList list;
    
    public ArrayWrapper(final Class clazz) {
        super(clazz);
        this.list = new ArrayList();
        assert clazz.isArray();
    }
    
    public Object createPrototype() {
        throw new UnsupportedOperationException("createPrototype not supported.");
    }
    
    public void add(final Object o) {
        this.list.add(o);
    }
    
    public void add(final int n, final Object o) {
        this.list.add(n, o);
    }
    
    public boolean isTyped() {
        return true;
    }
    
    public Class componentType() {
        return this.type.getComponentType();
    }
    
    protected Object createObject() {
        final String arrayComponentName = ReflectionUtil.arrayComponentName(ReflectionUtil.className(this.getType()));
        final Class classForName = ReflectionUtil.classForName(arrayComponentName);
        if (classForName == null) {
            throw new YamlException("class " + arrayComponentName + " cannot be resolved.");
        }
        final Object instance = Array.newInstance(classForName, this.list.size());
        for (int i = 0; i < Array.getLength(instance); ++i) {
            try {
                Array.set(instance, i, this.list.get(i));
            }
            catch (IllegalArgumentException ex) {
                throw new YamlException("Fail to set " + this.list.get(i) + " into array of type " + arrayComponentName);
            }
        }
        return instance;
    }
    
    protected void fireCreated() {
        this.list = this.toList(this.object);
        super.fireCreated();
    }
    
    ArrayList toList(final Object o) {
        final ArrayList<Object> list = new ArrayList<Object>(Array.getLength(o));
        for (int i = 0; i < Array.getLength(o); ++i) {
            list.add(Array.get(o, i));
        }
        return list;
    }
    
    public int size() {
        return this.list.size();
    }
    
    public boolean isOrdered() {
        return true;
    }
    
    public Iterator iterator() {
        return this.list.iterator();
    }
}
