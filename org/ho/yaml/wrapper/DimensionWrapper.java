package org.ho.yaml.wrapper;

import java.awt.*;

public class DimensionWrapper extends DelayedCreationBeanWrapper
{
    public DimensionWrapper(final Class clazz) {
        super(clazz);
    }
    
    public String[] getPropertyNames() {
        return new String[] { "width", "height" };
    }
    
    protected Object createObject() {
        return new Dimension(this.values.get("width").intValue(), this.values.get("height").intValue());
    }
    
    public Object createPrototype() {
        return new Dimension();
    }
}
