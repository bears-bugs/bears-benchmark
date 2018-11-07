package org.jsapar.utils.cache;

public interface Cache<K, V> {
    /**
     * @param key The key
     * @return A value stored in cache or null if there is none.
     */
    V get(K key);


    /**
     * Put a new value to the cache
     * @param key The key
     * @param value The value
     */
    void put(K key, V value);
}
