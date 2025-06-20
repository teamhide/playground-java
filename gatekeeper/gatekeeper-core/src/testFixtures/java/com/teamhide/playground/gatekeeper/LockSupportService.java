package com.teamhide.playground.gatekeeper;

import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class LockSupportService {
    private final LockRegistry lockRegistry;

    public LockSupportService(final LockRegistry lockRegistry) {
        this.lockRegistry = lockRegistry;
    }

    public boolean executeWithFunctionalLock(final String lockName, final String identifier, final Supplier<Boolean> supplier) {
        final LockManager lockManager = lockRegistry.lockManager(lockName);
        return lockManager.executeWithLock(identifier, supplier);
    }
}
