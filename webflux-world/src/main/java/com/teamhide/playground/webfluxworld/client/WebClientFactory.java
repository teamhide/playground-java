package com.teamhide.playground.webfluxworld.client;

import com.teamhide.playground.webfluxworld.client.properties.ClientProperties;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
public class WebClientFactory {
    private WebClientFactory() {}

    public static WebClient create(final String clientName, final ClientProperties properties) {
        final ConnectionProvider connectionProvider = ConnectionProvider.builder(clientName)
                .maxConnections(properties.getMaxConnections())
                .build();
        final HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.getConnectTimeoutMillis())
                .doOnConnected(connection ->
                        connection.addHandlerLast(new ReadTimeoutHandler(properties.getReadTimeoutMillis())));
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter((request, next) ->
                        next.exchange(request)
                                .flatMap(response -> handleResponseErrors(clientName, response))
                                .transform(mono -> applyRetry(mono, properties, clientName))
                )
                .build();
    }

    private static Mono<ClientResponse> handleResponseErrors(final String clientName, final ClientResponse response) {
        if (response.statusCode().is4xxClientError()) {
            log.error("[{}] Client error: {}", clientName, response.statusCode());
            return Mono.error(new ClientError());
        } else if (response.statusCode().is5xxServerError()) {
            log.error("[{}] Server error: {}", clientName, response.statusCode());
            return Mono.error(new ServerError());
        }
        return Mono.just(response);
    }

    public static <T> Mono<T> applyRetry(Mono<T> mono, ClientProperties properties, String clientName) {
        return mono.retryWhen(Retry.backoff(properties.getRetryMaxAttempts(), Duration.ofMillis(properties.getRetryBackoffMillis()))
                .filter(e -> e instanceof IllegalStateException)
                .doAfterRetry(signal -> log.warn("[{}] Retrying, attempt: {}", clientName, signal.totalRetries()))
                .onRetryExhaustedThrow((retrySpec, retrySignal) -> {
                    throw new IllegalStateException();
                })
        );
    }
}
