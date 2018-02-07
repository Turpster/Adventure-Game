package org.ho.yaml.wrapper;

import org.ho.yaml.*;
import java.util.*;
import org.ho.yaml.exception.*;
import java.lang.reflect.*;

public abstract class AbstractWrapper implements ObjectWrapper
{
    protected Class type;
    protected Object object;
    protected List<CreateListener> listeners;
    protected boolean objectInitialized;
    protected YamlConfig config;
    
    protected AbstractWrapper(final Class type) {
        this.listeners = new ArrayList<CreateListener>();
        this.objectInitialized = false;
        this.type = type;
    }
    
    protected void fireCreated() {
        final Iterator<CreateListener> iterator = this.listeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().created(this.object);
        }
    }
    
    protected Object createObject() {
        try {
            if (this.config.isConstructorAccessibleForDecoding(this.type)) {
                final Constructor<Object> declaredConstructor = this.type.getDeclaredConstructor((Class<?>[])null);
                declaredConstructor.setAccessible(true);
                return declaredConstructor.newInstance(new Object[0]);
            }
            throw new ObjectCreationException("Default constructor for " + this.type + " is not accessible.");
        }
        catch (Exception ex) {
            throw new ObjectCreationException("Can't create object of type " + this.type + " using default constructor.", ex);
        }
    }
    
    public void addCreateHandler(final CreateListener createListener) {
        if (this.object == null) {
            this.listeners.add(createListener);
        }
        else {
            createListener.created(this.object);
        }
    }
    
    public Class getType() {
        return this.type;
    }
    
    public void setObject(final Object object) {
        if (object == null) {
            this.object = null;
            this.objectInitialized = true;
        }
        else {
            this.object = object;
            this.objectInitialized = true;
            this.fireCreated();
        }
    }
    
    public Object getObject() {
        if (!this.objectInitialized) {
            this.setObject(this.createObject());
            return this.object;
        }
        return this.object;
    }
    
    public Object createPrototype() {
        return this.createObject();
    }
    
    public String toString() {
        return (this.object == null) ? ("[" + this.getType() + "]") : ("[" + this.object + "]");
    }
    
    public ObjectWrapper makeWrapper() {
        return null;
    }
    
    public void setYamlConfig(final YamlConfig config) {
        this.config = config;
    }
}
