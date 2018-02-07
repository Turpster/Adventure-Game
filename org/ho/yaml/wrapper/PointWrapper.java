package org.ho.yaml.wrapper;

import java.awt.*;

public class PointWrapper extends DelayedCreationBeanWrapper
{
    public PointWrapper(final Class clazz) {
        super(clazz);
    }
    
    public String[] getPropertyNames() {
        return new String[] { "x", "y" };
    }
    
    protected Object createObject() {
        return new Point(this.values.get("x").intValue(), this.values.get("y").intValue());
    }
    
    public Object createPrototype() {
        return new Point();
    }
}
