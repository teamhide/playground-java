package com.teamhide.playground.retry;

import java.time.Duration;
import java.util.Set;
import java.util.function.Supplier;

public class Retryer {
    public static<T> T execute(final RetryConfig config, final Supplier<T> supplier) {
        int attempts = 0;
        int maxAttempts = config.getMaxAttempts();
        final Duration waitDuration = config.getWaitDuration();
        final Set<Class<? extends Throwable>> ignoreExceptions = config.getIgnoreExceptions();

        while (attempts <= maxAttempts) {
            try {
                return supplier.get();
            } catch (Exception e) {
                if (ignoreExceptions.contains(e.getClass())) {
                    throw e;
                }

                attempts++;
                if (attempts > maxAttempts) {
                    throw e;
                }

                try {
                    Thread.sleep(waitDuration.toMillis());
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                    throw new RetryException("Retry interrupted", interruptedException);
                }
            }
        }
        return supplier.get();
    }
}
