package com.teamhide.playground.distributedlock;

import com.teamhide.playground.distributedlock.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@IntegrationTest
@TestPropertySource(properties = {
    "lock.instances.sample.provider=redis",
    "lock.instances.sample.key=test-lock",
    "lock.instances.sample.waitTime=1000",
    "lock.instances.sample.leaseTime=1000",
})
class LockRegistryTest {
    @Autowired
    private LockRegistry lockRegistry;

    @Test
    @DisplayName("LockManager를 통해 분산락을 획득하고 실행할 수 있다")
    void testExecuteWithLock() {
        // Given
        final String identifier = UUID.randomUUID().toString();
        final AtomicInteger counter = new AtomicInteger(0);

        final LockManager manager = lockRegistry.lockManager("sample");

        // When
        final Integer result = manager.executeWithLock(identifier, () -> {
            counter.incrementAndGet();
            return 42;
        });

        // Then
        assertThat(result).isEqualTo(42);
        assertThat(counter.get()).isEqualTo(1);
    }

    @Test
    @DisplayName("application.yml에 설정하지 않은 락 이름을 가져오는 경우 예외가 발생한다")
    void testExecuteWithLockT() {
        // Given, When
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> lockRegistry.lockManager("sample2"));

        // Then
        assertThat(exception.getMessage()).contains("does not exist");
    }
}
