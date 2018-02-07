package org.ho.yaml.wrapper;

import org.ho.yaml.exception.*;

public class EnumWrapper extends AbstractWrapper implements SimpleObjectWrapper
{
    public EnumWrapper(final Class clazz) {
        super(clazz);
    }
    
    public Class expectedArgType() {
        return String.class;
    }
    
    public Object getOutputValue() {
        try {
            return this.getType().getMethod("name", (Class[])null).invoke(this.getObject(), (Object[])null);
        }
        catch (Exception ex) {
            throw new Error("Error getting enum value", ex);
        }
    }
    
    public void setObject(final Object object) {
        if (object instanceof String) {
            try {
                super.setObject(this.getType().getMethod("valueOf", String.class).invoke(this.getType(), object));
                return;
            }
            catch (Exception ex) {
                throw new YamlException("Problem getting " + object + " value of enum type " + this.type, ex);
            }
        }
        super.setObject(object);
    }
}
