package com.teamhide.playground.gatekeeper.mysql.reactive;

import com.teamhide.playground.gatekeeper.config.LockConfig;
import com.teamhide.playground.gatekeeper.util.KeyGenerator;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveMySqlLockManagerTest {
    @Mock
    private ConnectionFactory connectionFactory;

    private static final String key = "test-lock";

    private final LockConfig lockConfig = LockConfig.builder()
            .key(key)
            .waitTime(3000L)
            .leaseTime(10000L)
            .build();
    private ReactiveMySqlLockManager lockManager;

    @BeforeEach
    void setUp() {
        lockManager = new ReactiveMySqlLockManager(connectionFactory, lockConfig);
    }

    @Test
    @DisplayName("락 획득 이후 Mono<T> 리턴타입의 Supplier를 실행하고 락을 해제한다")
    void testExecuteWithLock() {
        // Given
        final String identifier = "1234";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Result releaseResult = mock(Result.class);
        final Statement releaseLockStatement = mock(Statement.class);

        // create connection
        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));

        // acquire
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(true));

        // bind & execute
        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseLockStatement);
        when(releaseLockStatement.bind(0, lockKey)).thenReturn(releaseLockStatement);
        when(releaseLockStatement.execute()).thenAnswer(invocation -> Mono.just(releaseResult));
        when(releaseResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(1));

        // close connection
        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Mono<String> result = lockManager.executeWithLock(identifier, () -> Mono.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectNext("OK")
                .verifyComplete();

        verify(acquireStatement, times(1)).execute();
        verify(releaseLockStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("락 획득 실패 시 예외가 발생한다")
    void testExecuteWithLockAcquireLockFails() {
        // Given
        final String identifier = "1234";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenReturn(Mono.error(new RuntimeException("DB Error")));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Mono<String> result = lockManager.executeWithLock(identifier, () -> Mono.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().contains("Failed to acquire lock"))
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("supplier 수행 중 예외 발생 시 락 해제 후 예외를 던진다")
    void testExecuteWithLockSupplierThrowsExceptionAfterAcquiringLock() {
        // Given
        final String identifier = "5678";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Statement releaseStatement = mock(Statement.class);
        final Result releaseResult = mock(Result.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(true));

        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseStatement);
        when(releaseStatement.bind(0, lockKey)).thenReturn(releaseStatement);
        when(releaseStatement.execute()).thenAnswer(invocation -> Mono.just(releaseResult));
        when(releaseResult.map(any(BiFunction.class))).thenReturn(Mono.just(1));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Mono<String> result = lockManager.executeWithLock(identifier, () -> Mono.error(new RuntimeException("Failure in business logic")));

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Failure in business logic"))
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(releaseStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("락 해제 시 예외가 발생하면 락 해제를 추가로 수행하지 않고 종료한다")
    void testExecuteWithLockReleaseThrowsException() {
        // Given
        final String identifier = "9999";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Statement releaseStatement = mock(Statement.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenReturn(Mono.just(true));

        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseStatement);
        when(releaseStatement.bind(0, lockKey)).thenReturn(releaseStatement);
        when(releaseStatement.execute()).thenReturn(Mono.error(new RuntimeException("Release failed")));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Mono<String> result = lockManager.executeWithLock(identifier, () -> Mono.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(RuntimeException.class);
                    assertThat(throwable.getMessage()).contains("Async resource cleanup failed");
                    assertThat(throwable.getCause()).isInstanceOf(IllegalStateException.class);
                    assertThat(throwable.getCause().getMessage()).contains("Failed to release lock for key");
                })
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(releaseStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("락 획득 이후 Flux<T> 리턴타입의 Supplier를 실행하고 락을 해제한다")
    void testExecuteManyWithLock() {
        // Given
        final String identifier = "1234";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Result releaseResult = mock(Result.class);
        final Statement releaseLockStatement = mock(Statement.class);

        // create connection
        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));

        // acquire
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(true));

        // bind & execute
        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseLockStatement);
        when(releaseLockStatement.bind(0, lockKey)).thenReturn(releaseLockStatement);
        when(releaseLockStatement.execute()).thenAnswer(invocation -> Mono.just(releaseResult));
        when(releaseResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(1));

        // close connection
        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Flux<String> result = lockManager.executeManyWithLock(identifier, () -> Flux.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectNext("OK")
                .verifyComplete();

        verify(acquireStatement, times(1)).execute();
        verify(releaseLockStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("락 획득 실패 시 예외가 발생한다")
    void testExecuteManyWithLockAcquireLockFails() {
        // Given
        final String identifier = "1234";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenReturn(Mono.error(new RuntimeException("DB Error")));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Flux<String> result = lockManager.executeManyWithLock(identifier, () -> Flux.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof IllegalStateException &&
                                throwable.getMessage().contains("Failed to acquire lock"))
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("supplier 수행 중 예외 발생 시 락 해제 후 예외를 던진다")
    void testExecuteManyWithLockSupplierThrowsExceptionAfterAcquiringLock() {
        // Given
        final String identifier = "5678";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Statement releaseStatement = mock(Statement.class);
        final Result releaseResult = mock(Result.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenAnswer(invocation -> Mono.just(true));

        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseStatement);
        when(releaseStatement.bind(0, lockKey)).thenReturn(releaseStatement);
        when(releaseStatement.execute()).thenAnswer(invocation -> Mono.just(releaseResult));
        when(releaseResult.map(any(BiFunction.class))).thenReturn(Mono.just(1));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Flux<String> result = lockManager.executeManyWithLock(identifier, () -> Flux.error(new RuntimeException("Failure in business logic")));

        // Then
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Failure in business logic"))
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(releaseStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }

    @Test
    @DisplayName("락 해제 시 예외가 발생하면 락 해제를 추가로 수행하지 않고 종료한다")
    void testExecuteManyWithLockReleaseThrowsException() {
        // Given
        final String identifier = "9999";
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        final Connection connection = mock(Connection.class);
        final Statement acquireStatement = mock(Statement.class);
        final Result acquireResult = mock(Result.class);
        final Statement releaseStatement = mock(Statement.class);

        when(connectionFactory.create()).thenAnswer(invocation -> Mono.just(connection));
        when(connection.createStatement("SELECT GET_LOCK(?, ?)")).thenReturn(acquireStatement);
        when(acquireStatement.bind(0, lockKey)).thenReturn(acquireStatement);
        when(acquireStatement.bind(1, timeoutSeconds)).thenReturn(acquireStatement);
        when(acquireStatement.execute()).thenAnswer(invocation -> Mono.just(acquireResult));
        when(acquireResult.map(any(BiFunction.class))).thenReturn(Mono.just(true));

        when(connection.createStatement("SELECT RELEASE_LOCK(?)")).thenReturn(releaseStatement);
        when(releaseStatement.bind(0, lockKey)).thenReturn(releaseStatement);
        when(releaseStatement.execute()).thenReturn(Mono.error(new RuntimeException("Release failed")));

        when(connection.close()).thenReturn(Mono.empty());

        // When
        final Flux<String> result = lockManager.executeManyWithLock(identifier, () -> Flux.just("OK"));

        // Then
        StepVerifier.create(result)
                .expectNext("OK")
                .expectErrorSatisfies(throwable -> {
                    assertThat(throwable).isInstanceOf(RuntimeException.class);
                    assertThat(throwable.getMessage()).contains("Async resource cleanup failed");
                    assertThat(throwable.getCause()).isInstanceOf(IllegalStateException.class);
                    assertThat(throwable.getCause().getMessage()).contains("Failed to release lock for key");
                })
                .verify();

        verify(acquireStatement, times(1)).execute();
        verify(releaseStatement, times(1)).execute();
        verify(connection, times(1)).close();
    }
}
