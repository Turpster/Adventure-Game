package yaml.parser;

public class YamlParserEvent implements ParserEvent
{
    int level;
    
    public YamlParserEvent() {
        this.level = 0;
    }
    
    public void event(final String s) {
    }
    
    public void error(final Exception ex, final int n) {
        System.out.println("Error near line " + n + ": " + ex);
    }
    
    public void event(final int n) {
        switch (n) {
            case 78:
            case 93:
            case 110:
            case 125: {
                --this.level;
                break;
            }
        }
        System.out.println(this.sp() + (char)n);
        switch (n) {
            case 91:
            case 123: {
                ++this.level;
                break;
            }
        }
    }
    
    public void content(final String s, final String s2) {
        System.out.println(this.sp() + s + " : <" + s2 + ">");
    }
    
    public void property(final String s, final String s2) {
        System.out.println(this.sp() + "( " + s + " : <" + s2 + "> )");
    }
    
    private String sp() {
        if (this.level < 0) {
            return "";
        }
        final char[] array = new char[this.level * 4];
        for (int i = 0; i < array.length; ++i) {
            array[i] = ' ';
        }
        return new String(array);
    }
}
