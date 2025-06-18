package com.teamhide.playground.gatekeeper;

import java.util.function.Supplier;

public interface LockManager {
    <T> T executeWithLock(String identifier, Supplier<T> supplier);
}
