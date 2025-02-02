package com.teamhide.playground.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class TokenBucketRateLimiter<T> implements RateLimiter<T> {
    private final int capacity;
    private final int refillRateSeconds;
    private long lastRefillTime;
    private final AtomicInteger bucket;
    private final ReentrantLock lock = new ReentrantLock();

    public TokenBucketRateLimiter(final int capacity, final int refillRateSeconds) {
        this.capacity = capacity;
        this.refillRateSeconds = refillRateSeconds;
        this.lastRefillTime = System.currentTimeMillis();
        this.bucket = new AtomicInteger(capacity);
    }

    @Override
    public T execute(final Supplier<T> function) {
        validateAndRefillToken();
        if (bucket.get() <= 0) {
            throw new RateLimitException();
        }
        bucket.decrementAndGet();
        return function.get();
    }

    private void validateAndRefillToken() {
        final long currentTime = System.currentTimeMillis();
        final long timeElapsed = currentTime - lastRefillTime;
        final long refillTokens = (timeElapsed / 1000) * refillRateSeconds;

        if (refillTokens > 0) {
            lock.lock();
            try {
                final long newTokens = Math.min(refillTokens, capacity - bucket.get());
                if (newTokens > 0) {
                    final int count = Math.toIntExact(newTokens);
                    bucket.addAndGet(count);
                    lastRefillTime = currentTime;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
