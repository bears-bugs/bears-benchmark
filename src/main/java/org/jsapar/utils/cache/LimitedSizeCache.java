package org.jsapar.utils.cache;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This implementation simply removes the oldest element when the max size is exceeded.
 */
public class LimitedSizeCache<K, V> implements Cache<K, V>{
    private final LinkedHashMap<K, V> elements;

    public LimitedSizeCache(final int maxSize) {
        this.elements = new LinkedHashMap<K, V>(Math.min(maxSize * 2, 1024)){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size()>maxSize;
            }
        };
    }

    @Override
    public V get(K key) {
        return elements.get(key);
    }

    @Override
    public void put(K key, V value) {
        elements.put(key, value);
    }


}
