package com.teamhide.playground.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ConfigurationProperties(prefix = "lock")
@Slf4j
public class LockRegistry implements InitializingBean {
    private final LockProviderRegistry lockProviderRegistry;
    private final ConcurrentHashMap<String, LockConfig> locks = new ConcurrentHashMap<>();
    private Map<String, LockProperties> instances;

    public LockRegistry(final LockProviderRegistry lockProviderRegistry) {
        this.lockProviderRegistry = lockProviderRegistry;
    }

    public LockManager lockManager(final String lockName) {
        final LockConfig lockConfig = locks.get(lockName);
        if (lockConfig == null) {
            throw new IllegalStateException("Lock '" + lockName + "' does not exist");
        }
        final LockProvider lockProvider = lockProviderRegistry.getProvider(lockConfig.getProvider());
        if (lockProvider == null) {
            throw new IllegalStateException("No LockProvider found for provider: " + lockConfig.getProvider());
        }
        return lockProvider.create(lockConfig);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (instances == null) {
            log.info("Lock instances is empty");
            return;
        }

        instances.forEach((key, value) -> {
            if (value.getProvider() == null || value.getKey() == null) {
                throw new IllegalStateException("Lock '" + key + "' has null field. provider: " + value.getProvider() + ", key: " + value.getKey());
            }
            locks.put(key, LockConfig.builder()
                    .provider(value.getProvider())
                    .key(value.getKey())
                    .waitTime(value.getWaitTime())
                    .leaseTime(value.getLeaseTime())
                    .build());
            log.info("Lock '{}' has been configured", key);
        });
    }

    public Map<String, LockProperties> getInstances() {
        return instances;
    }

    public void setInstances(final Map<String, LockProperties> instances) {
        this.instances = instances;
    }

    public static class LockProperties {
        private String provider;
        private String key;
        private long waitTime;
        private long leaseTime;

        public String getProvider() {
            return provider;
        }

        public String getKey() {
            return key;
        }

        public long getWaitTime() {
            return waitTime;
        }

        public long getLeaseTime() {
            return leaseTime;
        }

        public void setProvider(final String provider) {
            this.provider = provider;
        }

        public void setKey(final String key) {
            this.key = key;
        }

        public void setWaitTime(final long waitTime) {
            this.waitTime = waitTime;
        }

        public void setLeaseTime(final long leaseTime) {
            this.leaseTime = leaseTime;
        }
    }
}
