package com.teamhide.playground.bulkhead;

import java.util.function.Supplier;

public interface Bulkhead {
    <T> T execute(Supplier<T> supplier);

    void execute(Runnable runnable);
}
