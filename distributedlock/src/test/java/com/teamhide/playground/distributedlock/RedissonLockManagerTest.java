package com.teamhide.playground.distributedlock;

import com.teamhide.playground.distributedlock.config.LockConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RedissonLockManagerTest {
    @Mock
    RedissonClient redissonClient;

    @Mock
    RLock rLock;

    final LockConfig config = LockConfig.builder()
            .key("testKey")
            .waitTime(1000)
            .leaseTime(1000)
            .provider("redis")
            .build();
    RedissonLockManager lockManager;

    @BeforeEach
    void setUp() {
        lockManager = new RedissonLockManager(redissonClient, config);
    }

    @Test
    @DisplayName("성공적으로 락을 획득하고 로직을 실행한다")
    void executeWithLock_success() throws InterruptedException {
        // given
        final LockConfig config = LockConfig.builder()
                .key("test-lock")
                .waitTime(1000)
                .leaseTime(1000)
                .build();
        final RLock mockLock = mock(RLock.class);
        when(mockLock.tryLock(anyLong(), anyLong(), any())).thenReturn(true);
        when(mockLock.isHeldByCurrentThread()).thenReturn(true); // 👈 이게 중요
        doNothing().when(mockLock).unlock();

        final RedissonClient mockClient = mock(RedissonClient.class);
        when(mockClient.getLock("test-lock:test")).thenReturn(mockLock);

        final RedissonLockManager lockManager = new RedissonLockManager(mockClient, config);

        // when
        final String result = lockManager.executeWithLock("test", () -> "success");

        // then
        assertEquals("success", result);
        verify(mockLock).unlock();
    }

    @Test
    @DisplayName("락 획득 중 인터럽트가 발생하면 예외를 던진다")
    void executeWithLock_interrupted() throws InterruptedException {
        when(redissonClient.getLock("testKey:user1")).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenThrow(new InterruptedException("interrupted"));

        assertThatThrownBy(() -> lockManager.executeWithLock("user1", () -> "FAIL"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lock is null or held by other thread");

        verify(rLock, never()).unlock();
    }

    @Test
    @DisplayName("락을 현재 스레드가 보유하지 않은 상태에서 해제 시도시 예외 발생")
    void releaseLock_notHeldByCurrentThread() throws InterruptedException {
        when(redissonClient.getLock("testKey:user1")).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(false);

        assertThatThrownBy(() -> lockManager.executeWithLock("user1", () -> "VALUE"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Lock is null or held by other thread");
    }

    @Test
    @DisplayName("락 해제 중 IllegalMonitorStateException 발생 시 예외로 전파")
    void releaseLock_illegalMonitorState() throws InterruptedException {
        when(redissonClient.getLock("testKey:user1")).thenReturn(rLock);
        when(rLock.tryLock(anyLong(), anyLong(), eq(TimeUnit.MILLISECONDS))).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);
        doThrow(new IllegalMonitorStateException("not held")).when(rLock).unlock();

        assertThatThrownBy(() -> lockManager.executeWithLock("user1", () -> "FAIL"))
                .isInstanceOf(IllegalStateException.class)
                .hasCauseInstanceOf(IllegalMonitorStateException.class);
    }
}
