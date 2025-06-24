package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;
import com.teamhide.playground.gatekeeper.config.LockConfigLoader;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReactiveLockRegistry {
    private final ReactiveLockProviderRegistry lockProviderRegistry;
    private final LockConfigLoader lockConfigLoader;

    public ReactiveLockRegistry(final ReactiveLockProviderRegistry lockProviderRegistry, final LockConfigLoader lockConfigLoader) {
        this.lockProviderRegistry = lockProviderRegistry;
        this.lockConfigLoader = lockConfigLoader;
    }

    public ReactiveLockManager lockManager(final String lockName) {
        final LockConfig config = lockConfigLoader.get(lockName);
        final ReactiveLockProvider provider = lockProviderRegistry.get(config.getProvider());
        return provider.create(config);
    }
}
