package com.teamhide.playground.webfluxworld.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public class OrderClient {
    private static final String clientName = "orderClient";
    private final ClientProperties properties;
    private final WebClient webClient;

    public OrderClient(final ClientProperties properties) {
        this.properties = properties;
        this.webClient = WebClientFactory.create(clientName, properties);
    }

    public Mono<GetOrderResponse> getOrder() {
        final String path = "/order";
        final String uri = UriComponentsBuilder.fromPath(path).toUriString();
        final String fullUri = properties.getBaseUrl() + uri;
        log.info("PointClient | uri: {}", fullUri);

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new IllegalStateException()))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response -> Mono.error(new IllegalStateException()))
                .bodyToMono(GetOrderResponse.class)
                .onErrorResume(error -> {
                    log.error("OrderClient | error: {}", error.getMessage());
                    return Mono.error(IllegalStateException::new);
                })
                .retryWhen(
                        Retry.backoff(properties.getRetryMaxAttempts(), Duration.ofMillis(properties.getRetryBackoffMillis()))
                                .filter(e -> e instanceof IllegalStateException)
                                .doAfterRetry(Retry.RetrySignal::totalRetries)
                                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> {
                                    throw new IllegalStateException(retrySignal.toString());
                                }))
                )
                .doOnNext(response -> log.info("OrderClient | response: {}", response));
    }
}
