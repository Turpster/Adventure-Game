package org.ho.yaml.wrapper;

import org.ho.yaml.*;
import org.ho.yaml.exception.*;

public class OneArgConstructorTypeWrapper extends DefaultSimpleTypeWrapper implements WrapperFactory
{
    protected String argType;
    
    public OneArgConstructorTypeWrapper() {
        super(null);
    }
    
    public OneArgConstructorTypeWrapper(final Class clazz) {
        super(clazz);
    }
    
    public OneArgConstructorTypeWrapper(final Class clazz, final String argType) {
        this(clazz);
        this.argType = argType;
    }
    
    public Class expectedArgType() {
        return ReflectionUtil.classForName(this.argType);
    }
    
    public void setObject(Object instance) {
        if (instance == null) {
            super.setObject(null);
        }
        else if (instance.getClass() == this.getType()) {
            super.setObject(instance);
        }
        else {
            try {
                instance = this.type.getConstructor(this.expectedArgType()).newInstance(instance);
                super.setObject(instance);
            }
            catch (Exception ex) {
                throw new YamlException(ex);
            }
        }
    }
    
    public String getArgType() {
        return this.argType;
    }
    
    public void setArgType(final String argType) {
        this.argType = argType;
    }
    
    public void setType(final Class type) {
        this.type = type;
    }
    
    public ObjectWrapper makeWrapper() {
        final OneArgConstructorTypeWrapper oneArgConstructorTypeWrapper = new OneArgConstructorTypeWrapper(this.getType(), this.argType);
        oneArgConstructorTypeWrapper.setYamlConfig(this.config);
        return oneArgConstructorTypeWrapper;
    }
}
