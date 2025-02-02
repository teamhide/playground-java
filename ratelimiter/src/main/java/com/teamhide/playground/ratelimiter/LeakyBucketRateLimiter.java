package com.teamhide.playground.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class LeakyBucketRateLimiter<T> implements RateLimiter<T> {
    private final int capacity;
    private final int leakRatePerSecond;
    private final AtomicInteger bucket = new AtomicInteger(0);
    private final ReentrantLock lock = new ReentrantLock();
    private long lastLeakTime = System.currentTimeMillis();

    public LeakyBucketRateLimiter(final int capacity, final int leakRatePerSecond) {
        this.capacity = capacity;
        this.leakRatePerSecond = leakRatePerSecond;
    }

    @Override
    public T execute(final Supplier<T> function) {
        validateAndLeak();
        if (bucket.get() >= capacity) {
            throw new RateLimitException();
        }
        bucket.incrementAndGet();
        return function.get();
    }

    private void validateAndLeak() {
        final long currentTime = System.currentTimeMillis();
        final long timeElapsed = (currentTime - lastLeakTime) / 1000;
        final int tokensToLeak = Math.toIntExact((timeElapsed * leakRatePerSecond));

        if (tokensToLeak > 0) {
            lock.lock();
            try {
                int tokensToActuallyLeak = Math.min(tokensToLeak, bucket.get());
                bucket.addAndGet(-tokensToActuallyLeak);
                lastLeakTime = currentTime;
            } finally {
                lock.unlock();
            }
        }
    }
}
