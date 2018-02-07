package org.ho.yaml;

import org.ho.util.*;
import org.ho.yaml.exception.*;
import java.util.*;
import org.ho.yaml.wrapper.*;

abstract class State
{
    Logger logger;
    YamlDecoder decoder;
    Map<String, ObjectWrapper> aliasMap;
    Stack<State> stack;
    ObjectWrapper wrapper;
    String declaredClassname;
    String anchorname;
    
    State(final Map<String, ObjectWrapper> aliasMap, final Stack<State> stack, final YamlDecoder decoder, final Logger logger) {
        this.aliasMap = aliasMap;
        this.stack = stack;
        this.decoder = decoder;
        this.logger = logger;
    }
    
    public void nextOnEvent(final int n) {
        switch (n) {
            case 123: {
                this.openMap(this.stack);
                break;
            }
            case 91: {
                this.openList(this.stack);
                break;
            }
        }
    }
    
    public void nextOnContent(final String s, final String s2) {
    }
    
    public void nextOnProperty(final String s, final String s2) {
        if ("transfer".equals(s)) {
            if (this.getDeclaredClassname() == null && s2.startsWith("!")) {
                this.setDeclaredClassname(ReflectionUtil.transfer2classname(s2.substring(1), this.decoder.getConfig()));
            }
        }
        else if ("anchor".equals(s) && s2.startsWith("&")) {
            this.setAnchorname(s2.substring(1));
        }
    }
    
    public abstract void childCallback(final ObjectWrapper p0);
    
    void clear() {
        this.setDeclaredClassname(null);
        this.setAnchorname(null);
    }
    
    ObjectWrapper createWrapper(final String s) {
        ObjectWrapper objectWrapper = this.decoder.getConfig().getWrapper(this.expectedType());
        if (objectWrapper == null) {
            objectWrapper = this.decoder.getConfig().getWrapper(s);
        }
        return objectWrapper;
    }
    
    protected String expectedType(final String s) {
        String expectedType = this.expectedType();
        if (expectedType == null && "string".equals(s)) {
            expectedType = "java.lang.String";
        }
        return expectedType;
    }
    
    protected String expectedType() {
        return this.getClassname();
    }
    
    void openMap(final Stack<State> stack) {
        final ObjectWrapper wrapper = this.createWrapper(ReflectionUtil.className(HashMap.class));
        if (this.getAnchorname() != null) {
            this.markAnchor(wrapper, this.getAnchorname());
        }
        final MapState mapState = new MapState(this.aliasMap, stack, this.decoder, this.logger);
        if (!(wrapper instanceof MapWrapper)) {
            throw new YamlException(wrapper.getObject() + " is not a Collection and so cannot be mapped from a sequence.");
        }
        mapState.wrapper = wrapper;
        stack.push(mapState);
    }
    
    void openList(final Stack<State> stack) {
        final ObjectWrapper wrapper = this.createWrapper(ReflectionUtil.className(ArrayList.class));
        if (this.getAnchorname() != null) {
            this.markAnchor(wrapper, this.getAnchorname());
        }
        final ListState listState = new ListState(this.aliasMap, stack, this.decoder, this.logger);
        if (!(wrapper instanceof CollectionWrapper)) {
            throw new YamlException(wrapper.getObject() + " is not a Collection and so cannot be mapped from a sequence.");
        }
        listState.wrapper = wrapper;
        stack.push(listState);
    }
    
    void markAnchor(final ObjectWrapper objectWrapper, final String s) {
        if (this.aliasMap.get(s) == null) {
            this.aliasMap.put(s, objectWrapper);
        }
    }
    
    public ObjectWrapper getWrapper() {
        return this.wrapper;
    }
    
    public void setWrapper(final ObjectWrapper wrapper) {
        this.wrapper = wrapper;
    }
    
    public String getClassname() {
        return this.declaredClassname;
    }
    
    public String getDeclaredClassname() {
        return this.declaredClassname;
    }
    
    public void setDeclaredClassname(final String declaredClassname) {
        this.declaredClassname = declaredClassname;
    }
    
    public String getAnchorname() {
        return this.anchorname;
    }
    
    public void setAnchorname(final String anchorname) {
        this.anchorname = anchorname;
    }
}
