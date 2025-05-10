package com.teamhide.playground.bulkhead;

public class BulkheadFactory {
    public static Bulkhead create(final BulkheadType type, final int maxConcurrency, final long timeoutMillis) {
        return switch (type) {
            case SEMAPHORE -> new SemaphoreBulkhead(maxConcurrency, timeoutMillis);
            case THREAD_POOL -> new ThreadPoolBulkhead(maxConcurrency, timeoutMillis);
        };
    }
}
