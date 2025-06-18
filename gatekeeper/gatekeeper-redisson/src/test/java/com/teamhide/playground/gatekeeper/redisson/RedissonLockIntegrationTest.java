package com.teamhide.playground.gatekeeper.redisson;

import com.teamhide.playground.gatekeeper.LockSupportService;
import com.teamhide.playground.gatekeeper.RedisIntegrationTest;
import com.teamhide.playground.gatekeeper.util.KeyGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RedisIntegrationTest
@TestPropertySource(properties = {
        "lock.instances.test-lock.provider=redis",
        "lock.instances.test-lock.key=test-lock",
        "lock.instances.test-lock.waitTime=1000",
        "lock.instances.test-lock.leaseTime=1000",
})
public class RedissonLockIntegrationTest {
    @Autowired
    private LockSupportService lockSupportService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("레디스를 통해 락을 걸고 원본 작업을 수행한다")
    void testLock() throws Exception {
        // Given
        final CountDownLatch lockAcquired = new CountDownLatch(1);
        final CountDownLatch testCompletion = new CountDownLatch(1);
        final String identifier = UUID.randomUUID().toString();
        final String lockName = "test-lock";
        final String lockKey = KeyGenerator.generate(lockName, identifier);

        // When
        CompletableFuture.runAsync(() -> lockSupportService.executeWithFunctionalLock(lockName, identifier, () -> {
            lockAcquired.countDown();
            try {
                testCompletion.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return true;
        }));

        lockAcquired.await(1, TimeUnit.SECONDS);
        final Boolean isLocked = redisTemplate.hasKey(lockKey);

        // Then
        assertThat(isLocked).isTrue();
        testCompletion.countDown();
    }
}
