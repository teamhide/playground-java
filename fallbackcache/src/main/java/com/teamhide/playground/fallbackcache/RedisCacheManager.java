package com.teamhide.playground.fallbackcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RedisCacheManager<V> implements CacheManager<V> {
    private final Map<String, V> cacheStore = new ConcurrentHashMap<>();

    @Override
    public V get(final String key) {
        return cacheStore.get(key);
    }

    @Override
    public void put(final String key, final V value) {
        cacheStore.put(key, value);
    }

    @Override
    public void delete(final String key) {
        cacheStore.remove(key);
    }

    @Override
    public void clear() {
        cacheStore.clear();
    }
}
