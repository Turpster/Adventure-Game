package org.ho.yaml.wrapper;

import java.awt.*;

public class ColorWrapper extends DelayedCreationBeanWrapper
{
    public ColorWrapper(final Class clazz) {
        super(clazz);
    }
    
    public String[] getPropertyNames() {
        return new String[] { "red", "green", "blue", "alpha" };
    }
    
    protected Object createObject() {
        final Color color = (Color)this.createPrototype();
        return new Color((this.values.containsKey("red") ? this.values.get("red").floatValue() : color.getRed()) / 255.0f, (this.values.containsKey("green") ? this.values.get("green").floatValue() : color.getGreen()) / 255.0f, (this.values.containsKey("blue") ? this.values.get("blue").floatValue() : color.getBlue()) / 255.0f, (this.values.containsKey("alpha") ? this.values.get("alpha").floatValue() : color.getAlpha()) / 255.0f);
    }
    
    public Object createPrototype() {
        return Color.BLACK;
    }
}
