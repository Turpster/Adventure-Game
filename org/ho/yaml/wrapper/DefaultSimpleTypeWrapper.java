package org.ho.yaml.wrapper;

public class DefaultSimpleTypeWrapper extends AbstractWrapper implements SimpleObjectWrapper
{
    public DefaultSimpleTypeWrapper(final Class clazz) {
        super(clazz);
    }
    
    public Class expectedArgType() {
        return this.type;
    }
    
    public Object getOutputValue() {
        return this.getObject();
    }
}
