package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;
import org.redisson.api.RedissonClient;

public class RedisLockProvider implements LockProvider {
    private final RedissonClient redissonClient;

    public RedisLockProvider(final RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public String type() {
        return "redis";
    }

    @Override
    public LockManager create(final LockConfig config) {
        return new RedissonLockManager(redissonClient, config);
    }
}
