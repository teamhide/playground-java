package com.teamhide.playground.fallbackcache;

import java.time.Duration;

public interface CacheManager<V> {
    V get(String key);

    Duration getExpiredDuration(String key);

    void put(String key, V value, Duration ttl);

    void delete(String key);

    void clear();
}
