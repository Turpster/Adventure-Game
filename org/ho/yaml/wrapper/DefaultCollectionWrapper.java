package org.ho.yaml.wrapper;

import java.util.*;

public class DefaultCollectionWrapper extends AbstractWrapper implements CollectionWrapper
{
    public DefaultCollectionWrapper(final Class clazz) {
        super(clazz);
    }
    
    public Collection getCollection() {
        return (Collection)this.getObject();
    }
    
    public void add(final Object o) {
        this.getCollection().add(o);
    }
    
    public void add(final int n, final Object o) {
        ((List)this.getCollection()).add(n, o);
    }
    
    public boolean isTyped() {
        return false;
    }
    
    public Class componentType() {
        return null;
    }
    
    public int size() {
        return this.getCollection().size();
    }
    
    public boolean isOrdered() {
        return List.class.isAssignableFrom(this.getType());
    }
    
    public Iterator iterator() {
        return this.getCollection().iterator();
    }
}
