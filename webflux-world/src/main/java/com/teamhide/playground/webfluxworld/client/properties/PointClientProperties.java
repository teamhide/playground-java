package com.teamhide.playground.webfluxworld.client.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clients.point")
public class PointClientProperties extends ClientProperties{
    PointClientProperties(
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
