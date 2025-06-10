package com.teamhide.playground.distributedlock;

import java.util.function.Supplier;

public interface LockManager {
    <T> T executeWithLock(String identifier, Supplier<T> supplier);
}
