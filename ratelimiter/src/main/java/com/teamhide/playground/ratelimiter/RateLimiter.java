package com.teamhide.playground.ratelimiter;

import java.util.function.Supplier;

public interface RateLimiter<T> {
    T execute(Supplier<T> function);
    default void execute(Runnable function) {
        execute(() -> {
            function.run();
            return null;
        });
    }
}
