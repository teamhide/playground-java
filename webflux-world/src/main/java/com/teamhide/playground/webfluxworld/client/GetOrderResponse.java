package com.teamhide.playground.webfluxworld.client;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOrderResponse {
    private String orderId;

    @Override
    public String toString() {
        return "GetOrderResponse{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
