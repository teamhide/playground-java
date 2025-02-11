package com.teamhide.playground.webfluxworld.controller;

import com.teamhide.playground.webfluxworld.controller.dto.GetOrderResponse;
import com.teamhide.playground.webfluxworld.controller.dto.GetPointResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class MockController {
    @GetMapping("/order")
    public Mono<GetOrderResponse> getOrder() {
        return Mono.just(GetOrderResponse.builder().orderId(1L).build());
    }

    @GetMapping("/point")
    public Mono<GetPointResponse> getPoint() {
        return Mono.just(GetPointResponse.builder().userId(1L).point(20000).build());
    }
}
