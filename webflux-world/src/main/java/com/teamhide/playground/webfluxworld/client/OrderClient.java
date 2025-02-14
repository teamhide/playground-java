package com.teamhide.playground.webfluxworld.client;

import com.teamhide.playground.webfluxworld.client.dto.GetOrderResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange(url = "/order")
public interface OrderClient {
    @GetExchange("")
    Mono<GetOrderResponse> getOrder();
}
