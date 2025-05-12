package com.teamhide.playground.retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RetryConfig {
    private int maxAttempts;
    private Duration waitDuration;
    private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

    public static Builder custom() {
        return new Builder();
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public Duration getWaitDuration() {
        return waitDuration;
    }

    public Set<Class<? extends Throwable>> getIgnoreExceptions() {
        return ignoreExceptions;
    }

    public static class Builder {
        private int maxAttempts;
        private Duration waitDuration;
        private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

        public Builder() {
        }

        public Builder(final int maxAttempts, final Duration waitDuration) {
            this.maxAttempts = maxAttempts;
            this.waitDuration = waitDuration;
        }

        public Builder maxAttempts(final int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder waitDuration(final Duration waitDuration) {
            this.waitDuration = waitDuration;
            return this;
        }

        @SafeVarargs
        public final Builder ignoreExceptions(final Class<? extends Throwable>... exceptions) {
            this.ignoreExceptions.addAll(Arrays.asList(exceptions));
            return this;
        }

        public RetryConfig build() {
            final RetryConfig config = new RetryConfig();
            config.maxAttempts = maxAttempts;
            config.waitDuration = waitDuration;
            return config;
        }
    }
}
