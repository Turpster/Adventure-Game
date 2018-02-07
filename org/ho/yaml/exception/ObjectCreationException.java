package org.ho.yaml.exception;

public class ObjectCreationException extends YamlException
{
    public ObjectCreationException() {
    }
    
    public ObjectCreationException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ObjectCreationException(final String s) {
        super(s);
    }
    
    public ObjectCreationException(final Throwable t) {
        super(t);
    }
}
