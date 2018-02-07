package yaml.parser;

public interface ParserEvent
{
    void event(final int p0);
    
    void event(final String p0);
    
    void content(final String p0, final String p1);
    
    void property(final String p0, final String p1);
    
    void error(final Exception p0, final int p1);
}
