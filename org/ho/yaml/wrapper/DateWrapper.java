package org.ho.yaml.wrapper;

import java.util.*;
import org.ho.yaml.exception.*;
import java.text.*;

public class DateWrapper extends DefaultSimpleTypeWrapper implements WrapperFactory
{
    public static final String DATEFORMAT_YAML = "yyyy-MM-dd hh:mm:ss";
    public static final String DATEFORMAT_ISO8601 = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'";
    
    public DateWrapper() {
        super(Date.class);
    }
    
    public Class expectedArgType() {
        return String.class;
    }
    
    public ObjectWrapper makeWrapper() {
        final DateWrapper dateWrapper = new DateWrapper();
        dateWrapper.setYamlConfig(this.config);
        return dateWrapper;
    }
    
    public void setObject(final Object object) {
        if (object == null) {
            super.setObject(null);
        }
        else if (object instanceof Date) {
            super.setObject(object);
        }
        else {
            final String s = (String)object;
            super.setObject(this.parseDate(s));
            if (!this.objectInitialized) {
                throw new YamlException("Can't instantiate " + this.getType() + " with literal " + s);
            }
        }
    }
    
    Date parseDate(final String s) {
        final DateFormat dateFormatter = this.config.getDateFormatter();
        if (dateFormatter != null) {
            try {
                return dateFormatter.parse(s);
            }
            catch (ParseException ex) {
                throw new YamlException("Error parsing date: '" + s + "'", ex);
            }
        }
        try {
            return new Date(Long.parseLong(s));
        }
        catch (NumberFormatException ex2) {
            return new Date(s);
        }
    }
    
    public Object getOutputValue() {
        return this.formateDate((Date)this.getObject());
    }
    
    String formateDate(final Date date) {
        final DateFormat dateFormatter = this.config.getDateFormatter();
        if (dateFormatter == null) {
            return "" + date.getTime();
        }
        return "\"" + dateFormatter.format(date) + "\"";
    }
}
