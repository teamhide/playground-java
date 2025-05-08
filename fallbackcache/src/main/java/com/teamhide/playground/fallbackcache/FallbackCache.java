package com.teamhide.playground.fallbackcache;

import java.time.Duration;
import java.util.function.Supplier;

public class FallbackCache<V> {
    private final CacheManager<V> firstCacheManager;
    private final CacheManager<V> secondCacheManager;
    private final Duration defaultTtl;

    public FallbackCache(final CacheManager<V> firstCacheManager, final CacheManager<V> secondCacheManager, final Duration defaultTtl) {
        this.firstCacheManager = firstCacheManager;
        this.secondCacheManager = secondCacheManager;
        this.defaultTtl = defaultTtl;
    }

    public V get(final String key, final Supplier<V> fallback) {
        final V localCacheValue = firstCacheManager.get(key);
        if (localCacheValue != null) return localCacheValue;

        final V redisValue = secondCacheManager.get(key);
        if (redisValue != null) {
            final Duration ttl = secondCacheManager.getExpiredDuration(key);
            firstCacheManager.put(key, redisValue, ttl);
            return redisValue;
        }

        final V fallbackValue = fallback.get();
        if (fallbackValue != null) {
            firstCacheManager.put(key, fallbackValue, defaultTtl);
            secondCacheManager.put(key, fallbackValue, defaultTtl);
        }
        return fallbackValue;
    }

    public void put(final String key, final V value, final Duration ttl) {
        firstCacheManager.put(key, value, ttl);
        secondCacheManager.put(key, value, ttl);
    }

    public void delete(final String key) {
        firstCacheManager.delete(key);
        secondCacheManager.delete(key);
    }
}
