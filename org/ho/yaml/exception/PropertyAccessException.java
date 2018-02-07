package org.ho.yaml.exception;

public class PropertyAccessException extends YamlException
{
    public PropertyAccessException() {
    }
    
    public PropertyAccessException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public PropertyAccessException(final String s) {
        super(s);
    }
    
    public PropertyAccessException(final Throwable t) {
        super(t);
    }
}
