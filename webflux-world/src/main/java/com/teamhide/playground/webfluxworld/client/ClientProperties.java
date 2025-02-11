package com.teamhide.playground.webfluxworld.client;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClientProperties {
    private final String baseUrl;
    private final Integer connectTimeoutMillis;
    private final Integer readTimeoutMillis;
    private final Integer retryMaxAttempts;
    private final Integer retryBackoffMillis;
    private final Integer maxConnections;
}
