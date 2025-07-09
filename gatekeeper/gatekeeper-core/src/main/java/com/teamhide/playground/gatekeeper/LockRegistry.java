package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;
import com.teamhide.playground.gatekeeper.config.LockConfigLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class LockRegistry implements InitializingBean {
    private final LockProviderRegistry providerRegistry;
    private final ReactiveLockProviderRegistry reactiveLockProviderRegistry;
    private final LockConfigLoader configLoader;

    private static final String REACTIVE_PROVIDER_SUFFIX = "-reactive";

    public LockRegistry(
            final LockProviderRegistry providerRegistry,
            final ReactiveLockProviderRegistry reactiveLockProviderRegistry,
            final LockConfigLoader configLoader) {
        this.providerRegistry = providerRegistry;
        this.reactiveLockProviderRegistry = reactiveLockProviderRegistry;
        this.configLoader = configLoader;
    }

    public LockManager lockManager(final String lockName) {
        final LockConfig config = configLoader.get(lockName);
        final LockProvider provider = providerRegistry.get(config.getProvider());
        return provider.create(config);
    }

    public ReactiveLockManager reactiveLockManager(final String lockName) {
        final LockConfig config = configLoader.get(lockName);
        final ReactiveLockProvider provider = reactiveLockProviderRegistry.get(config.getProvider());
        return provider.create(config);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        validateConfigs();
    }

    private void validateConfigs() {
        this.configLoader
                .getAllLockNames()
                .forEach(
                        lockName -> {
                            final LockConfig config = configLoader.get(lockName);
                            final String provider = config.getProvider();

                            final boolean isReactive = provider.endsWith(REACTIVE_PROVIDER_SUFFIX);
                            final boolean exists =
                                    isReactive
                                            ? reactiveLockProviderRegistry.contains(provider)
                                            : providerRegistry.contains(provider);

                            if (!exists) {
                                throw new DistributedLockException(
                                        "No LockProvider found for type '"
                                                + provider
                                                + "' required by lock '"
                                                + lockName
                                                + "'");
                            }
                        });
    }
}
