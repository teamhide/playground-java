package com.teamhide.playground.bulkhead;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolBulkheadTest {
    @Test
    void testCaseOfSuccess() {
        // Given
        final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(2, 1_000_000); // 1s

        // When
        final String sut = bulkhead.execute(() -> "ok");

        // Then
        assertThat(sut).isEqualTo("ok");
    }

    @Test
    void testTimeoutException() {
        // Given
        final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(1, 100_000); // 100ms

        // When / Then
        assertThrows(BulkheadException.class, () ->
                bulkhead.execute(() -> {
                    try {
                        Thread.sleep(300); // exceeds 100ms
                        return "done";
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return "interrupted";
                    }
                })
        );
    }

    @Test
    void testCaseOfIllegalArgumentException() {
        // Given
        final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(1, 1_000_000);

        // When / Then
        final BulkheadException ex = assertThrows(BulkheadException.class, () ->
                bulkhead.execute(() -> {
                    throw new IllegalArgumentException("Boom");
                })
        );
        assertInstanceOf(ExecutionException.class, ex.getCause());
    }

    @Test
    void testRunnableCaseOfSuccess() {
        // Given
        final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(1, 1_000_000);
        final AtomicBoolean flag = new AtomicBoolean(false);

        // When
        bulkhead.execute(() -> flag.set(true));

        // Then
        assertTrue(flag.get());
    }

    @Test
    void testTimeoutRunnable() {
        // Given
        final ThreadPoolBulkhead bulkhead = new ThreadPoolBulkhead(1, 100_000); // 100ms

        // When / Then
        assertThrows(BulkheadException.class, () ->
                bulkhead.execute(() -> {
                    try {
                        Thread.sleep(500); // exceeds timeout
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                })
        );
    }
}
