package com.teamhide.playground.bulkhead;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SemaphoreBulkheadTest {
    @Test
    void testSupplierSuccess() {
        // Given
        final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 1_000_000); // 1초

        // When
        final String sut = bulkhead.execute(() -> "success");

        // Then
        assertThat("success").isEqualTo(sut);
    }

    @Test
    void testRunnableSuccess() {
        // Given
        final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 1_000_000);
        final AtomicBoolean executed = new AtomicBoolean(false);

        // When
        bulkhead.execute(() -> executed.set(true));

        // Then
        assertTrue(executed.get());
    }

    @Test
    void testTimeoutException() {
        // Given
        final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 100_000); // 100ms

        // When: 한 쓰레드가 세마포어를 점유한 상태에서, 두 번째 요청 시도
        final Thread holder = new Thread(() -> {
            bulkhead.execute(() -> {
                try {
                    Thread.sleep(500); // 500ms 점유
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            });
        });
        holder.start();

        try {
            Thread.sleep(50); // 세마포어 점유 확정 대기
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Then
        final BulkheadException ex = assertThrows(BulkheadException.class, () ->
                bulkhead.execute(() -> "blocked")
        );
        assertTrue(ex.getMessage().contains("timeout"));
    }

    @Test
    void testWrapExceptionInBulkheadException() {
        // Given
        final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 1_000_000);

        // When, Then
        final BulkheadException ex = assertThrows(BulkheadException.class, () ->
                bulkhead.execute(() -> {
                    throw new IllegalArgumentException("boom");
                })
        );
        assertInstanceOf(IllegalArgumentException.class, ex.getCause());
    }

    @Test
    void testCaseOfInterrupted() throws InterruptedException {
        // Given
        final SemaphoreBulkhead bulkhead = new SemaphoreBulkhead(1, 5_000_000); // 5s

        // Then
        final Thread testThread = new Thread(() -> {
            final BulkheadException ex = assertThrows(BulkheadException.class, () ->
                    bulkhead.execute(() -> {
                        try {
                            Thread.sleep(5000);
                            return "should not reach";
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return null;
                        }
                    })
            );
            assertTrue(ex.getMessage().contains("Interrupted"));
        });

        // When
        testThread.start();
        Thread.sleep(100);
        testThread.interrupt();
        testThread.join();
    }
}
