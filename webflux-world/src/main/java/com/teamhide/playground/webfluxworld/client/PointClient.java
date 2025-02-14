package com.teamhide.playground.webfluxworld.client;

import com.teamhide.playground.webfluxworld.client.dto.GetPointResponse;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;

@HttpExchange(url = "/point")
public interface PointClient {
    @GetExchange("")
    Mono<GetPointResponse> getPoint();
}
