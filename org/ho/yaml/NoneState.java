package org.ho.yaml;

import org.ho.yaml.wrapper.*;
import java.util.*;
import org.ho.util.*;

class NoneState extends State
{
    NoneState(final Map<String, ObjectWrapper> map, final Stack<State> stack, final YamlDecoder yamlDecoder, final Logger logger) {
        super(map, stack, yamlDecoder, logger);
    }
    
    public void childCallback(final ObjectWrapper wrapper) {
        this.wrapper = wrapper;
    }
    
    public void nextOnContent(final String s, final String s2) {
        this.wrapper = this.decoder.getConfig().getWrapperSetContent(this.expectedType(s), s2);
    }
}
