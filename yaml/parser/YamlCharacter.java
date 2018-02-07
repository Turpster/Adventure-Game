package yaml.parser;

public final class YamlCharacter
{
    public static final int PRINTABLE = 1;
    public static final int WORD = 2;
    public static final int LINE = 3;
    public static final int LINESP = 4;
    public static final int SPACE = 5;
    public static final int LINEBREAK = 6;
    public static final int DIGIT = 7;
    public static final int INDENT = 8;
    public static final int EOF = -1;
    
    public static boolean is(final char c, final int n) {
        switch (n) {
            case 1: {
                return isPrintableChar(c);
            }
            case 2: {
                return isWordChar(c);
            }
            case 3: {
                return isLineChar(c);
            }
            case 4: {
                return isLineSpChar(c);
            }
            case 5: {
                return isSpaceChar(c);
            }
            case 6: {
                return isLineBreakChar(c);
            }
            case 7: {
                return Character.isDigit(c);
            }
            case 8: {
                return c == ' ';
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean is(final int n, final int n2) {
        if (n == -1) {
            return false;
        }
        final char c = (char)n;
        switch (n2) {
            case 1: {
                return isPrintableChar(c);
            }
            case 2: {
                return isWordChar(c);
            }
            case 3: {
                return isLineChar(c);
            }
            case 4: {
                return isLineSpChar(c);
            }
            case 5: {
                return isSpaceChar(c);
            }
            case 6: {
                return isLineBreakChar(c);
            }
            case 7: {
                return Character.isDigit(c);
            }
            case 8: {
                return c == ' ';
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isPrintableChar(final char c) {
        return (c >= ' ' && c <= '~') || (c == '\t' || c == '\n' || c == '\r' || c == '\u0085') || (c >= ' ' && c <= '\ud7ff') || (c >= '\ue000' && c <= '\ufffd');
    }
    
    public static boolean isLineChar(final char c) {
        return c != ' ' && c != '\t' && c != '\n' && c != '\r' && c != '\u0085' && isPrintableChar(c);
    }
    
    public static boolean isLineSpChar(final char c) {
        return c != '\n' && c != '\r' && c != '\u0085' && isPrintableChar(c);
    }
    
    public static boolean isWordChar(final char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '-';
    }
    
    public static boolean isSpaceChar(final char c) {
        return c == '\t' || c == ' ';
    }
    
    public static boolean isLineBreakChar(final char c) {
        return c == '\n' || c == '\r' || c == '\u0085' || c == '\u2028' || c == '\u2029';
    }
    
    public static boolean isIndicator(final char c) {
        return "-:[]{},?*&!|#@%^'\"".indexOf(c) != -1;
    }
    
    public static boolean isIndicatorSpace(final char c) {
        return ":-".indexOf(c) != -1;
    }
    
    public static boolean isIndicatorInline(final char c) {
        return "[]{},".indexOf(c) != -1;
    }
    
    public static boolean isIndicatorNonSpace(final char c) {
        return "?*&!]|#@%^\"'".indexOf(c) != -1;
    }
    
    public static boolean isIndicatorSimple(final char c) {
        return ":[]{},".indexOf(c) != -1;
    }
    
    public static boolean isLooseIndicatorSimple(final char c) {
        return "[]{},".indexOf(c) != -1;
    }
}
