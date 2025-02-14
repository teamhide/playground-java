package com.teamhide.playground.webfluxworld.client;

import com.teamhide.playground.webfluxworld.client.properties.ClientProperties;
import com.teamhide.playground.webfluxworld.client.properties.OrderClientProperties;
import com.teamhide.playground.webfluxworld.client.properties.PointClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@EnableConfigurationProperties({
        OrderClientProperties.class,
        PointClientProperties.class,
})
public class ClientConfig {
    private final static String ORDER_CLIENT = "orderClient";
    private final static String POINT_CLIENT = "pointClient";

    @Bean
    public OrderClient orderClient(final OrderClientProperties properties) {
        return createClient(OrderClient.class, ORDER_CLIENT, properties);
    }

    @Bean
    public PointClient pointClient(final PointClientProperties properties) {
        return createClient(PointClient.class, POINT_CLIENT, properties);
    }

    private <T> T createClient(final Class<T> clazz, final String clientName, final ClientProperties clientProperties) {
        final WebClient webClient = WebClientFactory.create(clientName, clientProperties);
        final WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        final HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return proxyFactory.createClient(clazz);
    }
}
