package com.teamhide.playground.bulkhead;

import java.util.function.Supplier;

public interface Bulkhead {
    <T> T execute(Supplier<T> supplier, Supplier<T> fallback);

    void execute(Runnable runnable, Runnable fallback);
}
