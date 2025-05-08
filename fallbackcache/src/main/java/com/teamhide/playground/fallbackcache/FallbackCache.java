package com.teamhide.playground.fallbackcache;

import java.util.function.Supplier;

public class FallbackCache<V> {
    private final CacheManager<V> firstCacheManager;
    private final CacheManager<V> secondCacheManager;

    public FallbackCache(final CacheManager<V> firstCacheManager, final CacheManager<V> secondCacheManager) {
        this.firstCacheManager = firstCacheManager;
        this.secondCacheManager = secondCacheManager;
    }

    public V get(final String key, final Supplier<V> fallback) {
        final V localCacheValue = firstCacheManager.get(key);
        if (localCacheValue != null) return localCacheValue;

        final V redisValue = secondCacheManager.get(key);
        if (redisValue != null) {
            firstCacheManager.put(key, redisValue);
            return redisValue;
        }

        final V fallbackValue = fallback.get();
        if (fallbackValue != null) {
            firstCacheManager.put(key, fallbackValue);
            secondCacheManager.put(key, fallbackValue);
        }
        return fallbackValue;
    }

    public void put(final String key, final V value) {
        firstCacheManager.put(key, value);
        secondCacheManager.put(key, value);
    }

    public void delete(final String key) {
        firstCacheManager.delete(key);
        secondCacheManager.delete(key);
    }
}
