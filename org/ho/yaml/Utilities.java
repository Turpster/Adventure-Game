package org.ho.yaml;

import org.ho.yaml.exception.*;

public class Utilities
{
    public static Object decodeSimpleType(final String s) {
        if ("~".equals(s)) {
            return null;
        }
        try {
            return new Integer(s);
        }
        catch (NumberFormatException ex) {
            try {
                return new Double(s);
            }
            catch (NumberFormatException ex2) {}
            catch (NullPointerException ex3) {}
            if ("true".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s)) {
                return new Boolean(s);
            }
            return s;
        }
    }
    
    public static String quote(final Object o) {
        return "\"" + o + "\"";
    }
    
    public static String stringify(final Object o) {
        return stringify(o, "");
    }
    
    public static String escape(String s) {
        s = s.replace("\\", "\\\\");
        s = s.replace("\b", "\\b");
        s = s.replace("\u0000", "\\0");
        s = s.replace("\t", "\\t");
        s = s.replace("\"", "\\\"");
        return s;
    }
    
    public static String unescape(final String s) {
        if (s == null) {
            return null;
        }
        final StringBuffer sb = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 == '\\' && i != s.length() - 1) {
                final char char2 = s.charAt(i + 1);
                switch (char2) {
                    case 98: {
                        sb.append('\b');
                        break;
                    }
                    case 48: {
                        sb.append('\0');
                        break;
                    }
                    case 116: {
                        sb.append('\t');
                        break;
                    }
                    case 110: {
                        sb.append('\n');
                        break;
                    }
                    case 34: {
                        sb.append('\"');
                        break;
                    }
                    case 92: {
                        sb.append('\\');
                        break;
                    }
                    default: {
                        sb.append("" + char1 + char2);
                        break;
                    }
                }
                ++i;
            }
            else {
                sb.append(char1);
            }
        }
        return sb.toString();
    }
    
    public static String stringify(final Object o, final String s) {
        final String string = o.toString();
        if (string.indexOf(10) != -1) {
            if (string.length() == 1) {
                return quote("\\n");
            }
            final StringBuffer sb = new StringBuffer();
            sb.append("|");
            final String[] split = string.split("\n");
            for (int i = 0; i < split.length; ++i) {
                sb.append("\n" + s + split[i]);
            }
            if (string.charAt(string.length() - 1) == '\n') {
                sb.append("\n" + s);
            }
            return sb.toString();
        }
        else {
            if ("".equals(string)) {
                return quote(string);
            }
            final String s2 = ":[]{},\"'|*&";
            boolean b = false;
            final char[] charArray = s2.toCharArray();
            for (int length = charArray.length, j = 0; j < length; ++j) {
                if (string.indexOf(charArray[j]) != -1) {
                    b = true;
                    break;
                }
            }
            if (string.trim().length() != string.length()) {
                b = true;
            }
            if (isNumeric(string)) {
                b = true;
            }
            String quote;
            if (b) {
                quote = quote(escape(string));
            }
            else {
                quote = string;
            }
            return quote;
        }
    }
    
    static boolean isNumeric(final String s) {
        try {
            Long.parseLong(s);
            return true;
        }
        catch (Exception ex) {
            try {
                Double.parseDouble(s);
                return true;
            }
            catch (Exception ex2) {
                return false;
            }
        }
    }
    
    static String quote(final String s) {
        return "\"" + s + "\"";
    }
    
    public static Object convertType(String s, final Class clazz) {
        if ("~".equals(s)) {
            return null;
        }
        if (clazz == Integer.class || clazz == Integer.TYPE) {
            return new Integer(s.toString());
        }
        if (clazz == String.class) {
            return s;
        }
        if (clazz == Long.class || clazz == Long.TYPE) {
            return new Long(s.toString());
        }
        if (clazz == Short.class || clazz == Short.TYPE) {
            return new Short(s.toString());
        }
        if (clazz == Double.class || clazz == Double.TYPE) {
            return new Double(s.toString());
        }
        if (clazz == Boolean.class || clazz == Boolean.TYPE) {
            return new Boolean(s.toString());
        }
        if (clazz == Character.class || clazz == Character.TYPE) {
            s = s;
            return new Character(s.charAt(0));
        }
        return decodeSimpleType(s);
    }
    
    public static Class getWrapperClass(final Class clazz) {
        if (Integer.TYPE == clazz) {
            return Integer.class;
        }
        if (Double.TYPE == clazz) {
            return Double.class;
        }
        if (Float.TYPE == clazz) {
            return Float.class;
        }
        if (Boolean.TYPE == clazz) {
            return Boolean.class;
        }
        if (Character.TYPE == clazz) {
            return Character.class;
        }
        if (Byte.TYPE == clazz) {
            return Byte.class;
        }
        if (Long.TYPE == clazz) {
            return Long.class;
        }
        if (Short.TYPE == clazz) {
            return Short.class;
        }
        if (Character.TYPE == clazz) {
            return Character.class;
        }
        throw new YamlException(clazz + " is not a primitive type.");
    }
    
    public static boolean classEquals(final Class clazz, final Class clazz2) {
        if (clazz == clazz2) {
            return true;
        }
        if (clazz == null || clazz2 == null || (!clazz.isPrimitive() && !clazz2.isPrimitive())) {
            return false;
        }
        if (clazz.isPrimitive()) {
            return getWrapperClass(clazz) == clazz2;
        }
        return clazz == getWrapperClass(clazz2);
    }
    
    public static boolean same(final Object o, final Object o2) {
        if (o != null) {
            return o.equals(o2);
        }
        return o2 == null || o2.equals(o);
    }
}
