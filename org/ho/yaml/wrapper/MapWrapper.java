package org.ho.yaml.wrapper;

import java.util.*;

public interface MapWrapper extends ObjectWrapper
{
    Collection keys();
    
    Object get(final Object p0);
    
    void put(final Object p0, final Object p1);
    
    Class getExpectedType(final Object p0);
    
    boolean containsKey(final Object p0);
}
