package com.teamhide.playground.gatekeeper.mysql.reactive;

import com.teamhide.playground.gatekeeper.ReactiveLockManager;
import com.teamhide.playground.gatekeeper.config.LockConfig;
import com.teamhide.playground.gatekeeper.util.KeyGenerator;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class ReactiveMySqlLockManager implements ReactiveLockManager {
    private final ConnectionFactory connectionFactory;
    private final LockConfig lockConfig;

    public ReactiveMySqlLockManager(final ConnectionFactory connectionFactory, final LockConfig lockConfig) {
        this.connectionFactory = connectionFactory;
        this.lockConfig = lockConfig;
    }

    @Override
    public <T> Mono<T> executeWithLock(final String identifier, final Supplier<Mono<T>> supplier) {
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        return Mono.usingWhen(
                connectionFactory.create(),
                conn -> acquire(conn, lockKey, timeoutSeconds)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Failed to acquire lock for key: " + lockKey)))
                        .flatMap(acquired -> supplier.get()),
                conn -> release(conn, lockKey).thenReturn(conn.close())
        );
    }

    @Override
    public <T> Flux<T> executeManyWithLock(final String identifier, final Supplier<Flux<T>> supplier) {
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        return Flux.usingWhen(
                connectionFactory.create(),
                conn -> acquire(conn, lockKey, timeoutSeconds)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Failed to acquire lock for key: " + lockKey)))
                        .flatMapMany(acquired -> supplier.get()),
                conn -> release(conn, lockKey).thenReturn(conn.close())
        );
    }

    private Mono<Boolean> acquire(final Connection connection, final String lockKey, final int timeoutSeconds) {
        return Mono.from(connection.createStatement("SELECT GET_LOCK(?, ?)")
                        .bind(0, lockKey)
                        .bind(1, timeoutSeconds)
                        .execute())
                .flatMap(result -> Mono.from(result.map((row, meta) -> {
                    final Long success = row.get(0, Long.class);
                    return success != null && success == 1L;
                })))
                .doOnSuccess(r -> log.info("Acquired lock for key: {}", lockKey))
                .doOnError(e -> log.error("Failed to acquire lock for key: {}", lockKey))
                .onErrorMap(e -> new IllegalStateException("Failed to acquire lock for key: " + lockKey));
    }

    private Mono<Void> release(final Connection connection, final String lockKey) {
        return Mono.from(connection.createStatement("SELECT RELEASE_LOCK(?)")
                        .bind(0, lockKey)
                        .execute())
                .flatMap(result -> Mono.from(result.map((row, meta) -> row.get(0))))
                .doOnSuccess(r -> log.info("Released lock for key: {}", lockKey))
                .doOnError(e -> log.error("Failed to release lock for key: {}", lockKey))
                .onErrorMap(e -> new IllegalStateException("Failed to release lock for key: " + lockKey))
                .then();
    }
}
