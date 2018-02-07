package org.ho.yaml.wrapper;

import org.ho.yaml.exception.*;
import java.util.*;

public abstract class DelayedCreationBeanWrapper extends DefaultBeanWrapper
{
    protected Map<String, Object> values;
    protected HashSet<String> keys;
    
    public DelayedCreationBeanWrapper(final Class clazz) {
        super(clazz);
        this.values = new HashMap<String, Object>();
        this.keys = new HashSet<String>(Arrays.asList(this.getPropertyNames()));
    }
    
    public void setProperty(final String s, final Object o) throws PropertyAccessException {
        this.values.put(s, o);
    }
    
    public Object getProperty(final String s) throws PropertyAccessException {
        if (this.values.containsKey(s)) {
            return this.values.get(s);
        }
        return super.getProperty(s);
    }
    
    public Set keys() {
        return this.keys;
    }
    
    public abstract String[] getPropertyNames();
}
