package com.teamhide.playground.retry;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RetryConfigTest {
    @Test
    void testBuild() {
        // Given
        final int maxAttempts = 3;
        final Duration waitDuration = Duration.ofSeconds(5);

        // When
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(maxAttempts)
                .waitDuration(waitDuration)
                .build();

        // Then
        assertThat(config.getMaxAttempts()).isEqualTo(maxAttempts);
        assertThat(config.getWaitDuration()).isEqualTo(waitDuration);
    }
}
