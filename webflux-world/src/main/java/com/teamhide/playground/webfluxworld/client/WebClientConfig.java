package com.teamhide.playground.webfluxworld.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {
    @Bean
    public OrderClient orderClient() {
        final ClientProperties properties = ClientProperties.builder()
                .baseUrl("http://localhost:8080")
                .connectTimeoutMillis(2000)
                .readTimeoutMillis(2000)
                .retryMaxAttempts(1)
                .retryBackoffMillis(1000)
                .maxConnections(50)
                .build();
        return new OrderClient(properties);
    }

    @Bean
    public PointClient pointClient() {
        final ClientProperties properties = ClientProperties.builder()
                .baseUrl("http://localhost:8080")
                .connectTimeoutMillis(2000)
                .readTimeoutMillis(2000)
                .retryMaxAttempts(1)
                .retryBackoffMillis(1000)
                .maxConnections(50)
                .build();
        return new PointClient(properties);
    }
}
