package com.teamhide.playground.bulkhead;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class SemaphoreBulkhead implements Bulkhead {
    private final Semaphore semaphore;
    private final long timeoutMillis;

    public SemaphoreBulkhead(final int maxConcurrent, final long timeoutMillis) {
        this.semaphore = new Semaphore(maxConcurrent);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public <T> T execute(final Supplier<T> supplier, final Supplier<T> fallback) {
        if (!semaphore.tryAcquire()) return fallback.get();
        try {
            return supplier.get();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public void execute(final Runnable runnable, final Runnable fallback) {
        if (!semaphore.tryAcquire()) {
            fallback.run();
            return;
        }
        try {
            runnable.run();
        } finally {
            semaphore.release();
        }
    }

    private <T> T doExecute(final Supplier<T> supplier, final Supplier<T> fallback) throws TimeoutException {
        final boolean acquired;
        try {
            acquired = semaphore.tryAcquire(timeoutMillis, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return fallback.get();
        }

        if (!acquired) {
            throw new TimeoutException("Bulkhead timeout");
        }

        try {
            return supplier.get();
        } finally {
            semaphore.release();
        }
    }
}
