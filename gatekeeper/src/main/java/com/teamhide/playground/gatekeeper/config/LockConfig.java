package com.teamhide.playground.gatekeeper.config;

public class LockConfig {
    private String provider;
    private String key;
    private long waitTime;
    private long leaseTime;

    private LockConfig() {}

    public static Builder builder() {
        return new Builder();
    }

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

    public static class Builder {
        private String provider;
        private String key;
        private long waitTime;
        private long leaseTime;

        private Builder() {}

        public Builder provider(final String provider) {
            this.provider = provider;
            return this;
        }

        public Builder key(final String key) {
            this.key = key;
            return this;
        }

        public Builder waitTime(final long waitTime) {
            this.waitTime = waitTime;
            return this;
        }

        public Builder leaseTime(final long leaseTime) {
            this.leaseTime = leaseTime;
            return this;
        }

        public LockConfig build() {
            final LockConfig lockConfig = new LockConfig();
            lockConfig.provider = this.provider;
            lockConfig.key = this.key;
            lockConfig.waitTime = this.waitTime;
            lockConfig.leaseTime = this.leaseTime;
            return lockConfig;
        }
    }
}
