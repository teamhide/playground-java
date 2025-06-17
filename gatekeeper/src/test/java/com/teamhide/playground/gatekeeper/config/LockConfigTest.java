package com.teamhide.playground.gatekeeper.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LockConfigTest {
    @Test
    void testBuilder() {
        // Given, When
        final LockConfig config = LockConfig.builder()
                .key("name")
                .waitTime(1000)
                .leaseTime(1000)
                .build();

        // Then
        assertThat(config.getKey()).isEqualTo("name");
        assertThat(config.getWaitTime()).isEqualTo(1000);
        assertThat(config.getLeaseTime()).isEqualTo(1000);
    }
}
