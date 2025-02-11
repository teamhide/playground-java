package com.teamhide.playground.webfluxworld.client;

import lombok.Getter;

@Getter
public class GetOrderResponse {
    private String orderId;

    @Override
    public String toString() {
        return "GetOrderResponse{" +
                "orderId='" + orderId + '\'' +
                '}';
    }
}
