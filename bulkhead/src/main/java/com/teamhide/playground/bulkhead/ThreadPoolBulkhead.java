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
    public <T> T execute(final Supplier<T> supplier, final Supplier<T> fallback) {
        final Future<T> future = executor.submit(supplier::get);
        return doExecute(future, fallback);
    }

    @Override
    public void execute(final Runnable runnable, final Runnable fallback) {
        final Future<?> future = executor.submit(runnable);
        doExecute(future, () -> {
            fallback.run();
            return null;
        });
    }

    private <T> T doExecute(final Future<T> future, final Supplier<T> fallback) {
        try {
            return future.get(timeoutMillis, TimeUnit.MICROSECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return fallback.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return fallback.get();
        }
    }
}
