package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;
import com.teamhide.playground.gatekeeper.config.LockConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockRegistry {
    private final LockProviderRegistry lockProviderRegistry;
    private final ReactiveLockProviderRegistry reactiveLockProviderRegistry;
    private final LockConfigLoader lockConfigLoader;

    public LockRegistry(
            final LockProviderRegistry lockProviderRegistry,
            final ReactiveLockProviderRegistry reactiveLockProviderRegistry,
            final LockConfigLoader lockConfigLoader
    ) {
        this.lockProviderRegistry = lockProviderRegistry;
        this.reactiveLockProviderRegistry = reactiveLockProviderRegistry;
        this.lockConfigLoader = lockConfigLoader;
    }

    public LockManager lockManager(final String lockName) {
        final LockConfig config = lockConfigLoader.get(lockName);
        final LockProvider provider = lockProviderRegistry.get(config.getProvider());
        return provider.create(config);
    }

    public ReactiveLockManager reactiveLockManager(final String lockName) {
        final LockConfig config = lockConfigLoader.get(lockName);
        final ReactiveLockProvider provider = reactiveLockProviderRegistry.get(config.getProvider());
        return provider.create(config);
    }
}
