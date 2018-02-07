package org.ho.yaml.wrapper;

public interface CollectionWrapper extends ObjectWrapper, Iterable
{
    void add(final Object p0);
    
    void add(final int p0, final Object p1);
    
    boolean isTyped();
    
    Class componentType();
    
    int size();
    
    boolean isOrdered();
}
