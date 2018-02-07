package org.ho.yaml.wrapper;

import java.util.*;

public class DefaultMapWrapper extends AbstractWrapper implements MapWrapper
{
    public DefaultMapWrapper(final Class clazz) {
        super(clazz);
    }
    
    protected Map getMap() {
        return (Map)this.getObject();
    }
    
    public boolean containsKey(final Object o) {
        return this.getMap().containsKey(o);
    }
    
    public Object get(final Object o) {
        return this.getMap().get(o);
    }
    
    public void put(final Object o, final Object o2) {
        this.getMap().put(o, o2);
    }
    
    public Class getExpectedType(final Object o) {
        return null;
    }
    
    public Set keys() {
        return this.getMap().keySet();
    }
}
