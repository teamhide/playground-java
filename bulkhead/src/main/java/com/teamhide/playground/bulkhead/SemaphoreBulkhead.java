package com.teamhide.playground.bulkhead;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class SemaphoreBulkhead implements Bulkhead {

    private final Semaphore semaphore;
    private final long timeoutMillis;

    public SemaphoreBulkhead(final int maxConcurrent, final long timeoutMillis) {
        this.semaphore = new Semaphore(maxConcurrent);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public <T> T execute(final Supplier<T> supplier) {
        return doExecute(supplier);
    }

    @Override
    public void execute(final Runnable runnable) {
        doExecute(() -> {
            runnable.run();
            return null;
        });
    }

    private <T> T doExecute(final Supplier<T> supplier) {
        try {
            boolean acquired = semaphore.tryAcquire(timeoutMillis, TimeUnit.MICROSECONDS);
            if (!acquired) {
                throw new BulkheadException("Bulkhead timeout after " + timeoutMillis);
            }

            try {
                return supplier.get();
            } catch (Exception e) {
                throw new BulkheadException("Exception during execution", e);
            } finally {
                semaphore.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new BulkheadException("Interrupted while waiting for permit", e);
        }
    }
}
