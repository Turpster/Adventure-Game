package yaml.parser;

public class SyntaxException extends Exception
{
    public int line;
    
    public SyntaxException() {
    }
    
    public SyntaxException(final String s) {
        super(s);
    }
    
    public SyntaxException(final String s, final int line) {
        super(s);
        this.line = line;
    }
}
