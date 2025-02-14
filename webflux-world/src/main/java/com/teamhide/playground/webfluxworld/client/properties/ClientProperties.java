package com.teamhide.playground.webfluxworld.client.properties;

import lombok.Getter;

@Getter
public abstract class ClientProperties {
    private final String baseUrl;
    private final Integer connectTimeoutMillis;
    private final Integer readTimeoutMillis;
    private final Integer retryMaxAttempts;
    private final Integer retryBackoffMillis;
    private final Integer maxConnections;

    public ClientProperties(
            final String baseUrl,
            final Integer connectTimeoutMillis,
            final Integer readTimeoutMillis,
            final Integer retryMaxAttempts,
            final Integer retryBackoffMillis,
            final Integer maxConnections
    ) {
        this.baseUrl = baseUrl;
        this.connectTimeoutMillis = connectTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.retryMaxAttempts = retryMaxAttempts;
        this.retryBackoffMillis = retryBackoffMillis;
        this.maxConnections = maxConnections;
    }
}
