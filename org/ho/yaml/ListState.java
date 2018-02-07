package org.ho.yaml;

import org.ho.util.*;
import org.ho.yaml.wrapper.*;
import java.util.*;

class ListState extends State
{
    ListState(final Map<String, ObjectWrapper> map, final Stack<State> stack, final YamlDecoder yamlDecoder, final Logger logger) {
        super(map, stack, yamlDecoder, logger);
    }
    
    CollectionWrapper getCollection() {
        return (CollectionWrapper)this.getWrapper();
    }
    
    public void nextOnContent(final String s, final String s2) {
        if (s2.length() > 0 && "alias".equals(s) && this.aliasMap.containsKey(s2.substring(1))) {
            this.aliasMap.get(s2.substring(1)).addCreateHandler(new ObjectWrapper.CreateListener() {
                final /* synthetic */ int val$position = ListState.this.getCollection().size();
                
                public void created(final Object o) {
                    if (ListState.this.getCollection().isOrdered()) {
                        ListState.this.getCollection().add(this.val$position, o);
                    }
                    else {
                        ListState.this.getCollection().add(o);
                    }
                }
            });
        }
        else {
            final ObjectWrapper wrapperSetContent = this.decoder.getConfig().getWrapperSetContent(this.expectedType(s), s2);
            if (this.getAnchorname() != null) {
                this.markAnchor(wrapperSetContent, this.getAnchorname());
            }
            this.getCollection().add(wrapperSetContent.getObject());
        }
        this.clear();
    }
    
    public void nextOnEvent(final int n) {
        switch (n) {
            case 93: {
                this.stack.pop();
                this.stack.peek().childCallback(this.getWrapper());
                break;
            }
            default: {
                super.nextOnEvent(n);
                break;
            }
        }
    }
    
    protected String expectedType() {
        if (this.getCollection().isTyped()) {
            final Class componentType = this.getCollection().componentType();
            if (Object.class != componentType) {
                return ReflectionUtil.className(componentType);
            }
        }
        final String expectedType = super.expectedType();
        if (List.class.getName().equals(expectedType)) {
            return ArrayList.class.getName();
        }
        if (Map.class.getName().equals(expectedType)) {
            return HashMap.class.getName();
        }
        return expectedType;
    }
    
    public void childCallback(final ObjectWrapper objectWrapper) {
        this.getCollection().add(objectWrapper.getObject());
        this.clear();
    }
}
