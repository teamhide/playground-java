package com.teamhide.playground.webfluxworld.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients.order")
public class OrderClientProperties extends ClientProperties {
    public OrderClientProperties(
            final String baseUrl,
            final Integer connectTimeoutMillis,
            final Integer readTimeoutMillis,
            final Integer retryMaxAttempts,
            final Integer retryBackoffMillis,
            final Integer maxConnections
    ) {
        super(baseUrl, connectTimeoutMillis, readTimeoutMillis, retryMaxAttempts, retryBackoffMillis, maxConnections);
    }
}
