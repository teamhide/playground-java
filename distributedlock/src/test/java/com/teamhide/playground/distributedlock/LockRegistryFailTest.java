package com.teamhide.playground.distributedlock;

import com.teamhide.playground.distributedlock.support.RedisIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RedisIntegrationTest
@TestPropertySource(properties = {
    "lock.instances.sample.provider=mysql",
    "lock.instances.sample.key=test-lock",
    "lock.instances.sample.waitTime=1000",
    "lock.instances.sample.leaseTime=1000",
})
class LockRegistryFailTest {
    @Autowired
    private LockRegistry lockRegistry;

    @Test
    @DisplayName("application.yml에 설정한 provider에 해당하는 빈이 존재하지 않는 경우 예외가 발생한다")
    void testExecuteWithLock() {
        // Given, When
        final IllegalStateException exception = assertThrows(IllegalStateException.class, () -> lockRegistry.lockManager("sample"));

        // When
        assertThat(exception.getMessage()).isEqualTo("No LockProvider found for provider: mysql");
    }
}
