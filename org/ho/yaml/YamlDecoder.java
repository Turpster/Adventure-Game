package org.ho.yaml;

import org.ho.yaml.exception.*;
import org.ho.util.*;
import yaml.parser.*;
import java.io.*;
import java.util.*;

public class YamlDecoder
{
    BufferedReader in;
    YamlParser parser;
    YamlConfig config;
    
    YamlDecoder(final InputStream inputStream, final YamlConfig config) {
        this.config = YamlConfig.getDefaultConfig();
        this.config = config;
        try {
            this.in = new BufferedReader(new InputStreamReader(inputStream, config.encoding));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + config.encoding);
        }
    }
    
    public YamlDecoder(final InputStream inputStream) {
        this.config = YamlConfig.getDefaultConfig();
        try {
            this.in = new BufferedReader(new InputStreamReader(inputStream, this.config.encoding));
        }
        catch (UnsupportedEncodingException ex) {
            throw new YamlException("Unsupported encoding " + this.config.encoding);
        }
    }
    
    public YamlDecoder(final Reader reader) {
        this.config = YamlConfig.getDefaultConfig();
        this.in = new BufferedReader(reader);
    }
    
    public YamlDecoder(final Reader reader, final YamlConfig config) {
        this(reader);
        this.config = config;
    }
    
    public Object readObject() throws EOFException {
        try {
            final JYamlParserEvent event = new JYamlParserEvent(this.createLogger(), this);
            if (this.parser == null) {
                this.firstDocument(this.parser = new YamlParser(this.in, event), event);
            }
            else {
                this.parser.setEvent(event);
                if (!this.nextDocument(this.parser, event)) {
                    throw new EOFException();
                }
            }
            final Object bean = event.getBean();
            if (bean == null) {
                throw new YamlException("Document is empty.");
            }
            return bean;
        }
        catch (EOFException ex) {
            throw ex;
        }
        catch (YamlException ex2) {
            ex2.setLineNumber(this.parser.getLineNumber());
            throw ex2;
        }
    }
    
    public <T> YamlStream asStreamOfType(final Class<T> clazz) {
        return new Stream(clazz);
    }
    
    public YamlStream asStream() {
        return new Stream(Object.class);
    }
    
    Logger createLogger() {
        if (this.isSuppressWarnings()) {
            return new Logger(Logger.Level.NONE);
        }
        return new Logger();
    }
    
    void firstDocument(final YamlParser yamlParser, final YamlParserEvent yamlParserEvent) {
        try {
            while (yamlParser.comment(-1, false)) {}
            if (!yamlParser.header()) {
                yamlParser.document_first();
            }
            else {
                yamlParser.value_na(-1);
            }
        }
        catch (SyntaxException ex) {
            yamlParserEvent.error(ex, ex.line);
        }
        catch (IOException ex2) {
            throw new YamlException(ex2);
        }
    }
    
    boolean nextDocument(final YamlParser yamlParser, final YamlParserEvent yamlParserEvent) {
        try {
            return yamlParser.document_next();
        }
        catch (SyntaxException ex) {
            yamlParserEvent.error(ex, ex.line);
            return false;
        }
        catch (IOException ex2) {
            throw new YamlException(ex2);
        }
    }
    
    public <T> T readObjectOfType(final Class<T> clazz) throws EOFException {
        try {
            final JYamlParserEvent event = new JYamlParserEvent(clazz, this.createLogger(), this);
            if (this.parser == null) {
                this.firstDocument(this.parser = new YamlParser(this.in, event), event);
            }
            else {
                this.parser.setEvent(event);
                if (!this.nextDocument(this.parser, event)) {
                    throw new EOFException();
                }
            }
            final Object bean = event.getBean();
            if (bean == null) {
                throw new YamlException("Document is empty.");
            }
            return (T)bean;
        }
        catch (EOFException ex) {
            throw ex;
        }
    }
    
    public void close() {
        try {
            this.in.close();
        }
        catch (IOException ex) {}
    }
    
    public boolean isSuppressWarnings() {
        return this.config.isSuppressWarnings();
    }
    
    public void setSuppressWarnings(final boolean suppressWarnings) {
        this.config.setSuppressWarnings(suppressWarnings);
    }
    
    public YamlConfig getConfig() {
        return this.config;
    }
    
    public void setConfig(final YamlConfig config) {
        this.config = config;
    }
    
    private class Stream<T> implements YamlStream<T>
    {
        Class<T> clazz;
        T buffer;
        
        Stream(final Class<T> clazz) {
            this.clazz = clazz;
            this.peek();
        }
        
        private void peek() {
            try {
                if (this.clazz == Object.class) {
                    this.buffer = (T)YamlDecoder.this.readObject();
                }
                else {
                    this.buffer = YamlDecoder.this.readObjectOfType(this.clazz);
                }
            }
            catch (EOFException ex) {
                YamlDecoder.this.close();
                this.buffer = null;
            }
        }
        
        public boolean hasNext() {
            return this.buffer != null;
        }
        
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            final T buffer = this.buffer;
            this.peek();
            return buffer;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }
        
        public Iterator<T> iterator() {
            return this;
        }
    }
}
