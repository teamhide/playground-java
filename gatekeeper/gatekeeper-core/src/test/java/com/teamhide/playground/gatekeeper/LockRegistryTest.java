package com.teamhide.playground.gatekeeper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RedisIntegrationTest
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
    @DisplayName("application.yml에 설정하지 않은 락 이름을 가져오는 경우 예외가 발생한다")
    void testExecuteWithLockT() {
        // Given, When
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> lockRegistry.lockManager("sample2"));

        // Then
        assertThat(exception.getMessage()).isEqualTo("No LockConfig found for lockName: 'sample2'");
    }
}
