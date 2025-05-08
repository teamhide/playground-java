package com.teamhide.playground.fallbackcache;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LocalCacheManager<V> implements CacheManager<V> {
    private final Map<String, V> cacheStore = new ConcurrentHashMap<>();

    @Override
    public V get(final String key) {
        return cacheStore.get(key);
    }

    @Override
    public Duration getExpiredDuration(final String key) {
        return Duration.ofSeconds(10);
    }

    @Override
    public void put(final String key, final V value, final Duration ttl) {
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
