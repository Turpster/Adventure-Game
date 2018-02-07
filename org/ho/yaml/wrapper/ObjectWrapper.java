package org.ho.yaml.wrapper;

import org.ho.yaml.*;

public interface ObjectWrapper
{
    Object getObject();
    
    void setObject(final Object p0);
    
    Class getType();
    
    Object createPrototype();
    
    void setYamlConfig(final YamlConfig p0);
    
    void addCreateHandler(final CreateListener p0);
    
    public interface CreateListener
    {
        void created(final Object p0);
    }
}
