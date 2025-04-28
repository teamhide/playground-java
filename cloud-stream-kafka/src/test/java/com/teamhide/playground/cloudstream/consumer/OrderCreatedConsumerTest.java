package com.teamhide.playground.cloudstream.consumer;

import event.order.OrderCreated;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class OrderCreatedConsumerTest {
    private static final String bindingName = "orderCreated-out-0";

    @Autowired
    private StreamBridge streamBridge;

    @Test
    void testOrderCreated() {
        // Given
        final OrderCreated orderCreated = new OrderCreated(1L, 123L, 25900, "Chicken");
        final Message<OrderCreated> message = MessageBuilder.withPayload(orderCreated).build();
        final boolean send = streamBridge.send(bindingName, message);
        System.out.println("send = " + send);
    }
}
