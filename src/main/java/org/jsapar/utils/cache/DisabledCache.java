package org.jsapar.utils.cache;

/**
 * This implementation does not cache anything.
 * @param <K> Key type
 * @param <V> Value type
 */
public class DisabledCache <K, V> implements Cache <K, V>{

    @Override
    public V get(K key) {
        return null;
    }

    @Override
    public void put(K key, V value) {

    }
}
