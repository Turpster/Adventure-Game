package org.ho.yaml.wrapper;

import org.ho.yaml.exception.*;
import org.ho.yaml.*;

public class ClassWrapper extends OneArgConstructorTypeWrapper
{
    public ClassWrapper(final Class clazz) {
        super(clazz);
    }
    
    public void setObject(final Object object) {
        if (object == null) {
            super.setObject(null);
        }
        else if (object.getClass() == this.getType()) {
            super.setObject(object);
        }
        else {
            try {
                super.setObject(Class.forName((String)object));
            }
            catch (Exception ex) {
                throw new YamlException(ex);
            }
        }
    }
    
    public Object getOutputValue() {
        return ReflectionUtil.className((Class)this.getObject());
    }
}
