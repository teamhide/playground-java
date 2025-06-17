package com.teamhide.playground.distributedlock.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "lock")
public class LockInstances {
    private final Map<String, Property> instances;

    public LockInstances(final Map<String, Property> instances) {
        this.instances = instances;
    }

    public Map<String, Property> getInstances() {
        return instances;
    }

    public static class Property {
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
