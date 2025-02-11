package com.teamhide.playground.webfluxworld.client;

import org.springframework.web.reactive.function.client.WebClient;

public class OrderClient {
    private static final String clientName = "orderClient";
    private final WebClient webClient;

    public OrderClient(final ClientProperties properties) {
        this.webClient = WebClientFactory.create(clientName, properties);
    }
}
