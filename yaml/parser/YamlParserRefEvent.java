package yaml.parser;

import java.io.*;

public class YamlParserRefEvent implements ParserEvent
{
    PrintStream out;
    
    public YamlParserRefEvent() {
        this.out = System.out;
    }
    
    public YamlParserRefEvent(final File file) throws Exception {
        this.out = new PrintStream(new FileOutputStream(file));
    }
    
    public void event(final String s) {
    }
    
    public void error(final Exception ex, final int n) {
        this.out.println("error:  " + n);
    }
    
    public void event(final int n) {
        switch (n) {
            case 125: {
                this.out.println("}");
                break;
            }
            case 93: {
                this.out.println("]");
                break;
            }
            case 78: {
                this.out.println("}-");
                break;
            }
            case 110: {
                this.out.println("]-");
                break;
            }
            case 91: {
                this.out.println("[");
                break;
            }
            case 123: {
                this.out.println("{");
                break;
            }
        }
    }
    
    public void content(final String s, final String s2) {
        this.out.println(s + " : " + s2);
    }
    
    public void property(final String s, final String s2) {
        this.out.println(s + " : " + s2);
    }
}
