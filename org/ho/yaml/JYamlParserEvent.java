package org.ho.yaml;

import yaml.parser.*;
import org.ho.yaml.wrapper.*;
import org.ho.util.*;
import java.util.*;
import org.ho.yaml.exception.*;

class JYamlParserEvent extends YamlParserEvent
{
    Stack<State> stack;
    Map<String, ObjectWrapper> aliasMap;
    
    public JYamlParserEvent(final Logger logger, final YamlDecoder yamlDecoder) {
        this.stack = new Stack<State>();
        this.aliasMap = new HashMap<String, ObjectWrapper>();
        this.stack.push(new NoneState(this.aliasMap, this.stack, yamlDecoder, logger));
    }
    
    public JYamlParserEvent(final Object o, final Logger logger, final YamlDecoder yamlDecoder) {
        this.stack = new Stack<State>();
        this.aliasMap = new HashMap<String, ObjectWrapper>();
    }
    
    public JYamlParserEvent(final Class clazz, final Logger logger, final YamlDecoder yamlDecoder) {
        this(logger, yamlDecoder);
        this.stack.peek().setDeclaredClassname(ReflectionUtil.className(clazz));
    }
    
    public void content(final String s, final String s2) {
        this.stack.peek().nextOnContent(s, s2);
    }
    
    public void error(final Exception ex, final int n) {
        throw new YamlException(ex.getMessage(), n);
    }
    
    public void event(final int n) {
        this.stack.peek().nextOnEvent(n);
    }
    
    public void property(final String s, final String s2) {
        this.stack.peek().nextOnProperty(s, s2);
    }
    
    public Object getBean() {
        return this.stack.peek().getWrapper().getObject();
    }
}
