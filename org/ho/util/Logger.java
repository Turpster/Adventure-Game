package org.ho.util;

public class Logger
{
    Level level;
    
    public Logger() {
        this.level = Level.WARN;
    }
    
    public Logger(final Level level) {
        this.level = Level.WARN;
        this.level = level;
    }
    
    public void info(final Object o) {
        if (this.level == Level.INFO) {
            System.out.println("INFO: " + o);
        }
    }
    
    public void warn(final Object o) {
        if (this.level != Level.NONE) {
            System.err.println("WARNING: " + o);
        }
    }
    
    public enum Level
    {
        INFO, 
        WARN, 
        NONE;
    }
}
