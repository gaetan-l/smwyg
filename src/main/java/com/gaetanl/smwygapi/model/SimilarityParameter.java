package com.gaetanl.smwygapi.model;

import java.util.HashMap;

public class SimilarityParameter<K> extends HashMap<K, Integer> {
    /**
     * Increases similarity parameter by value. If key not in map, puts it. If
     * already in map, increases value.
     *
     * @param  key    the parameter to increase
     * @return        the new value
     */
    public Integer increase(final K key) {
        final Integer newValue = 1 + this.getOrDefault(key, 0);
        super.put(key, newValue);
        return newValue;
    }
}
