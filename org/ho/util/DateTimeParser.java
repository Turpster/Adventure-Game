package org.ho.util;

import java.util.*;
import java.text.*;

public class DateTimeParser extends DateFormat
{
    public static final String DATEFORMAT_ISO8601 = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
    public static final String DATEFORMAT_YAML = "yyyy-MM-dd hh:mm:ss";
    private static final String DATEFORMAT_TOSTRING = "EEE MMM dd hh:mm:ss z yyyy";
    private static final int FORMAT_NONE = -1;
    protected SimpleDateFormat outputFormat;
    protected ArrayList<Parser> parsers;
    
    public DateTimeParser() {
        this.parsers = new ArrayList<Parser>();
        this.outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
        this.setupParsers();
    }
    
    public DateTimeParser(final String s) {
        this.parsers = new ArrayList<Parser>();
        this.outputFormat = new SimpleDateFormat(s);
        this.setupParsers();
    }
    
    protected void setupParsers() {
        this.parsers.clear();
        this.parsers.add(new SimpleParser(this.outputFormat));
        this.parsers.add(new Parser() {
            public Date parse(final String s) throws ParseException {
                try {
                    return new Date(Long.parseLong(s));
                }
                catch (NumberFormatException ex) {
                    throw new ParseException("Error parsing value", -1);
                }
            }
        });
        this.parsers.add(new SimpleParser("yyyy-MM-dd"));
        this.parsers.add(new SimpleParser("EEE MMM dd hh:mm:ss z yyyy"));
        this.parsers.add(new SimpleParser(0, 0));
        this.parsers.add(new SimpleParser(1, 1));
        this.parsers.add(new SimpleParser(2, 2));
        this.parsers.add(new SimpleParser(3, 3));
        this.parsers.add(new SimpleParser(0, -1));
        this.parsers.add(new SimpleParser(1, -1));
        this.parsers.add(new SimpleParser(2, -1));
        this.parsers.add(new SimpleParser(3, -1));
        this.parsers.add(new SimpleParser(-1, 0));
        this.parsers.add(new SimpleParser(-1, 1));
        this.parsers.add(new SimpleParser(-1, 2));
        this.parsers.add(new SimpleParser(-1, 3));
    }
    
    public void addParser(final Parser parser) {
        this.parsers.add(parser);
    }
    
    public Date parse(final String s, final ParsePosition parsePosition) {
        final String substring = s.substring(parsePosition.getIndex());
        Date parse = null;
        for (final Parser parser : this.parsers) {
            try {
                parse = parser.parse(substring);
            }
            catch (ParseException ex) {
                continue;
            }
            break;
        }
        if (parse == null) {
            parsePosition.setIndex(parsePosition.getIndex());
            parsePosition.setErrorIndex(parsePosition.getIndex());
        }
        else {
            parsePosition.setIndex(substring.length());
        }
        return parse;
    }
    
    public StringBuffer format(final Date date, final StringBuffer sb, final FieldPosition fieldPosition) {
        return this.outputFormat.format(date, sb, fieldPosition);
    }
    
    protected class SimpleParser implements Parser
    {
        private DateFormat fmt;
        
        public SimpleParser(final String s) {
            this.fmt = new SimpleDateFormat(s);
        }
        
        public SimpleParser(final DateFormat fmt) {
            this.fmt = fmt;
        }
        
        public SimpleParser(final int n, final int n2) {
            if (n2 < 0) {
                this.fmt = DateFormat.getDateInstance(n);
            }
            else if (n < 0) {
                this.fmt = DateFormat.getTimeInstance(n2);
            }
            else {
                this.fmt = DateFormat.getDateTimeInstance(n, n2);
            }
        }
        
        public Date parse(final String s) throws ParseException {
            return this.fmt.parse(s);
        }
    }
    
    protected interface Parser
    {
        Date parse(final String p0) throws ParseException;
    }
}
