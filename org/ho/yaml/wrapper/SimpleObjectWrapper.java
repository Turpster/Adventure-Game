package org.ho.yaml.wrapper;

public interface SimpleObjectWrapper extends ObjectWrapper
{
    Class expectedArgType();
    
    Object getOutputValue();
}
