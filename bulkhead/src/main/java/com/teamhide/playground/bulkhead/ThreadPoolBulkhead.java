package com.teamhide.playground.bulkhead;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class ThreadPoolBulkhead implements Bulkhead {
    private final ExecutorService executor;
    private final long timeoutMillis;

    public ThreadPoolBulkhead(final int maxConcurrent, final long timeoutMillis) {
        this.executor = Executors.newFixedThreadPool(maxConcurrent);
        this.timeoutMillis = timeoutMillis;
    }

    @Override
    public <T> T execute(final Supplier<T> supplier) {
        final Future<T> future = executor.submit(supplier::get);
        return doExecute(future);
    }

    @Override
    public void execute(final Runnable runnable) {
        final Future<?> future = executor.submit(runnable);
        doExecute(future);
    }

    private <T> T doExecute(final Future<T> future) {
        try {
            return future.get(timeoutMillis, TimeUnit.MICROSECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new BulkheadException("Bulkhead timeout after " + timeoutMillis, e);
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new BulkheadException("Exception during execution", e);
        }
    }
}
