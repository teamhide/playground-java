package com.teamhide.playground.ratelimiter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenBucketRateLimiterTest {
    @Test
    @DisplayName("버킷에 토큰이 모두 소진된 경우 예외가 발생한다")
    void testException() {
        // Given
        final int capacity = 10;
        final int refillRateSeconds = 10;
        final TokenBucketRateLimiter<Void> rateLimiter = new TokenBucketRateLimiter<>(capacity, refillRateSeconds);

        // When, Then
        assertThrows(RateLimitException.class, () ->
                IntStream.range(0, capacity + 1).forEach(ignored -> rateLimiter.execute(this::run)));
    }

    @Test
    @DisplayName("버킷에 잔여 토큰이 존재하면 새로운 요청은 정상 수행된다")
    void testCaseOfSuccess() {
        // Given
        final int capacity = 10;
        final int refillRateSeconds = 10;
        final AtomicInteger executeCount = new AtomicInteger(0);
        final TokenBucketRateLimiter<Void> rateLimiter = new TokenBucketRateLimiter<>(capacity, refillRateSeconds);

        // When
        IntStream.range(0, capacity - 1).forEach(ignored -> {
            rateLimiter.execute(this::run);
            executeCount.incrementAndGet();
        });

        // Then
        assertThat(executeCount.get()).isEqualTo(capacity - 1);
    }

    @Test
    @DisplayName("잔여 토큰이 없어도 리필 시간이 되었다면 토큰이 추가되어 새로운 요청이 정상 수행된다")
    void testCaseOfRefill() {
        // Given
        final int capacity = 1;
        final int refillRateSeconds = 1;
        final AtomicInteger executeCount = new AtomicInteger(0);
        final TokenBucketRateLimiter<Void> rateLimiter = new TokenBucketRateLimiter<>(capacity, refillRateSeconds);

        // When
        IntStream.range(0, 2).forEach(ignored -> {
            rateLimiter.execute(this::run);
            executeCount.incrementAndGet();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // Then
        assertThat(executeCount.get()).isEqualTo(2);
    }

    private void run() {}
}
