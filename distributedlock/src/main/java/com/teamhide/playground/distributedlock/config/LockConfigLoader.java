package com.teamhide.playground.distributedlock.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class LockConfigLoader {
    private final LockInstances lockInstances;
    private final Map<String, LockConfig> lockConfigs = new ConcurrentHashMap<>();

    public LockConfigLoader(final LockInstances lockInstances) {
        this.lockInstances = lockInstances;
    }

    @PostConstruct
    public void init() {
        final Map<String, LockInstances.Property> instances = lockInstances.getInstances();
        if (instances == null || instances.isEmpty()) {
            log.info("Lock instances is empty");
            return;
        }
        instances.forEach((key, value) -> {
            if (value.getProvider() == null || value.getKey() == null) {
                throw new IllegalStateException("Lock '" + key + "' has no provider or key");
            }
            lockConfigs.put(key, LockConfig.builder()
                    .provider(value.getProvider())
                    .key(value.getKey())
                    .waitTime(value.getWaitTime())
                    .leaseTime(value.getLeaseTime())
                    .build());
            log.info("Lock instance '{}' loaded", key);
        });
    }

    public LockConfig get(final String lockName) {
        final LockConfig config = lockConfigs.get(lockName);
        if (config == null) {
            throw new IllegalStateException("No LockConfig found for lockName: '" + lockName + "'");
        }
        return config;
    }

    public Set<String> getAllLockNames() {
        return Collections.unmodifiableSet(lockConfigs.keySet());
    }
}
