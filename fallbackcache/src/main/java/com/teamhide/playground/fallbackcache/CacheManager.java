package com.teamhide.playground.fallbackcache;

public interface CacheManager<V> {
    V get(String key);

    void put(String key, V value);

    void delete(String key);

    void clear();
}
