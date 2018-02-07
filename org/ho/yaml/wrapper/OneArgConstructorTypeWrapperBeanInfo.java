package org.ho.yaml.wrapper;

import java.beans.*;

public class OneArgConstructorTypeWrapperBeanInfo extends SimpleBeanInfo
{
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            return new PropertyDescriptor[] { new PropertyDescriptor("type", OneArgConstructorTypeWrapper.class), new PropertyDescriptor("argType", OneArgConstructorTypeWrapper.class) };
        }
        catch (IntrospectionException ex) {
            throw new Error(ex.toString());
        }
    }
}
