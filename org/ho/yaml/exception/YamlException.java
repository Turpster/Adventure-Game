package org.ho.yaml.exception;

public class YamlException extends RuntimeException
{
    int lineNumber;
    
    public YamlException() {
    }
    
    public YamlException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public YamlException(final String s) {
        super(s);
    }
    
    public YamlException(final String s, final int lineNumber) {
        super(s);
        this.lineNumber = lineNumber;
    }
    
    public YamlException(final Throwable t) {
        super(t);
    }
    
    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }
    
    public String getMessage() {
        return "Error near line " + this.lineNumber + ": " + super.getMessage();
    }
}
