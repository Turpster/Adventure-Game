package org.ho.util;

import java.util.*;

public class BiDirectionalMap<K, V> extends HashMap<K, V>
{
    HashMap<V, K> reverse;
    
    public BiDirectionalMap() {
        this.reverse = new HashMap<V, K>();
    }
    
    public V put(final K k, final V v) {
        this.reverse.put(v, k);
        return super.put(k, v);
    }
    
    public V remove(final Object o) {
        final V remove = super.remove(o);
        this.reverse.remove(remove);
        return remove;
    }
    
    public Map<V, K> getReverse() {
        return this.reverse;
    }
}
