package com.teamhide.playground.webfluxworld.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderResponse {
    private final Long orderId;
}
