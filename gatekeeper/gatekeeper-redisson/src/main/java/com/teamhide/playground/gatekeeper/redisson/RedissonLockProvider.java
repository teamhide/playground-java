package com.teamhide.playground.gatekeeper.redisson;

import com.teamhide.playground.gatekeeper.LockManager;
import com.teamhide.playground.gatekeeper.LockProvider;
import com.teamhide.playground.gatekeeper.config.LockConfig;
import org.redisson.api.RedissonClient;

public class RedissonLockProvider implements LockProvider {
    private final RedissonClient redissonClient;

    public RedissonLockProvider(final RedissonClient redissonClient) {
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
