package org.ho.yaml;

import org.ho.util.*;
import org.ho.yaml.wrapper.*;
import java.util.*;

class MapState extends State
{
    String key;
    
    MapState(final Map<String, ObjectWrapper> map, final Stack<State> stack, final YamlDecoder yamlDecoder, final Logger logger) {
        super(map, stack, yamlDecoder, logger);
    }
    
    protected MapWrapper getMap() {
        return (MapWrapper)this.getWrapper();
    }
    
    public void nextOnContent(final String s, final String key) {
        if (this.key == null) {
            this.key = key;
        }
        else {
            if ("alias".equals(s)) {
                final String substring = key.substring(1);
                if (this.aliasMap.containsKey(substring)) {
                    this.aliasMap.get(substring).addCreateHandler(new ObjectWrapper.CreateListener() {
                        final /* synthetic */ String val$currentKey = MapState.this.key;
                        
                        public void created(final Object o) {
                            MapState.this.getMap().put(this.val$currentKey, o);
                        }
                    });
                }
            }
            else {
                final ObjectWrapper wrapperSetContent = this.decoder.getConfig().getWrapperSetContent(this.expectedType(s), key);
                this.getMap().put(Utilities.decodeSimpleType(this.key), wrapperSetContent.getObject());
                if (this.getAnchorname() != null) {
                    this.markAnchor(wrapperSetContent, this.getAnchorname());
                }
            }
            this.clear();
            this.key = null;
        }
    }
    
    public void nextOnEvent(final int n) {
        switch (n) {
            case 58: {
                break;
            }
            case 125: {
                this.stack.pop();
                this.stack.peek().childCallback(this.wrapper);
                break;
            }
            default: {
                super.nextOnEvent(n);
                break;
            }
        }
    }
    
    public void childCallback(final ObjectWrapper objectWrapper) {
        this.getMap().put(Utilities.decodeSimpleType(this.key), objectWrapper.getObject());
        this.clear();
        this.key = null;
    }
    
    protected String expectedType() {
        if (this.getClassname() != null) {
            return this.getClassname();
        }
        final Class expectedType = this.getMap().getExpectedType(this.key);
        if (expectedType == null) {
            return null;
        }
        final String className = ReflectionUtil.className(expectedType);
        if (List.class.getName().equals(className)) {
            return ArrayList.class.getName();
        }
        if (Map.class.getName().equals(className)) {
            return HashMap.class.getName();
        }
        return className;
    }
}
