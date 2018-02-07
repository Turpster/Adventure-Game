package com._Turpster.AdventureGame.Exceptions;

public class InvalidGameData extends Exception
{
    private static final long serialVersionUID = 7692831980438802181L;
    
    public InvalidGameData() {
    }
    
    public InvalidGameData(final String message) {
        super(message);
    }
    
    public InvalidGameData(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public InvalidGameData(final Throwable cause) {
        super(cause);
    }
}
