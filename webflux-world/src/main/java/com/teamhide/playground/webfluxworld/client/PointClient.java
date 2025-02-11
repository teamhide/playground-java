package com.teamhide.playground.webfluxworld.client;

import org.springframework.web.reactive.function.client.WebClient;

public class PointClient {
    private static final String clientName = "pointClient";
    private final WebClient webClient;

    public PointClient(final ClientProperties properties) {
        this.webClient = WebClientFactory.create(clientName, properties);
    }
}
