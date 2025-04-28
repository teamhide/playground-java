package com.teamhide.playground.cloudstream.consumer;

import event.order.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
@Configuration
public class OrderCreatedConsumer {
    @Bean
    public Function<OrderCreated, OrderCreated> orderCreated() {
        return orderCreated -> {
            log.info("OrderCreated: {}", orderCreated);
            return orderCreated;
        };
    }
}
