package com.teamhide.playground.distributedlock;

import com.teamhide.playground.distributedlock.util.KeyGenerator;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class RedissonLockManager implements LockManager {
    private final RedissonClient redissonClient;
    private final LockConfig config;

    public RedissonLockManager(final RedissonClient redissonClient, final LockConfig config) {
        this.redissonClient = redissonClient;
        this.config = config;
    }

    @Override
    public <T> T executeWithLock(final String identifier, final Supplier<T> supplier) {
        final String lockName = KeyGenerator.generate(config.getKey(), identifier);
        final RLock lock = redissonClient.getLock(lockName);
        try {
            lock.tryLock(config.getWaitTime(), config.getLeaseTime(), TimeUnit.MILLISECONDS);
            return supplier.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(e);
        } finally {
            release(lock);
        }
    }

    private void release(final RLock lock) {
        if (lock != null && lock.isHeldByCurrentThread()) {
            try {
                lock.unlock();
                return;
            } catch (IllegalMonitorStateException e) {
                throw new IllegalStateException(e);
            }
        }
        throw new IllegalStateException("Lock is null or held by other thread");
    }
}
