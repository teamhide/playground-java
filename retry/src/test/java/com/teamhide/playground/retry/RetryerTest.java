package com.teamhide.playground.retry;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RetryerTest {
    @Test
    void testCaseOfSuccess() {
        // Given
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(100))
                .build();

        // When
        final String sut = Retryer.execute(config, () -> "SUCCESS");

        // Then
        assertThat(sut).isEqualTo("SUCCESS");
    }

    @Test
    void testMaxAttempts() {
        // Given
        final AtomicInteger counter = new AtomicInteger(0);
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofMillis(50))
                .build();

        // When
        final String sut = Retryer.execute(config, () -> {
            if (counter.incrementAndGet() < 3) {
                throw new RuntimeException("fail");
            }
            return "RECOVERED";
        });

        // Then
        assertThat(sut).isEqualTo("RECOVERED");
        assertThat(counter.get()).isEqualTo(3);
    }

    @Test
    void testThrowExceptionAfterMaxAttempts() {
        // Given
        final AtomicInteger counter = new AtomicInteger(0);
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(2)
                .waitDuration(Duration.ofMillis(50))
                .build();

        // When, Then
        assertThrows(RuntimeException.class, () ->
                Retryer.execute(config, () -> {
                    counter.incrementAndGet();
                    throw new RuntimeException("fail");
                })
        );
        assertThat(counter.get()).isEqualTo(3);
    }

    @Test
    void testThrowExceptionOnInterrupt() throws InterruptedException {
        // Given
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(1000))
                .build();

        // When, Then
        final Thread testThread = new Thread(() -> {
            assertThrows(RetryException.class, () ->
                    Retryer.execute(config, () -> {
                        throw new RuntimeException("fail");
                    })
            );
        });

        testThread.start();
        Thread.sleep(200);
        testThread.interrupt();
        testThread.join();
    }

    @Test
    void testIgnoreExceptions() {
        // Given
        final RetryConfig config = RetryConfig.custom()
                .maxAttempts(5)
                .waitDuration(Duration.ofMillis(100))
                .ignoreExceptions(IllegalStateException.class)
                .build();
        final AtomicInteger counter = new AtomicInteger(0);

        // When, Then
        assertThrows(IllegalStateException.class, () ->
                Retryer.execute(config, () -> {
                    counter.incrementAndGet();
                    throw new IllegalStateException("ignored");
                })
        );
        assertThat(counter.get()).isEqualTo(6);
    }
}
