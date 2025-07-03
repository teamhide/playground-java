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
    private static final String GET_LOCK_STATEMENT = "SELECT GET_LOCK(?, ?)";
    private static final String RELEASE_LOCK_STATEMENT = "SELECT RELEASE_LOCK(?)";

    public ReactiveMySqlLockManager(final ConnectionFactory connectionFactory, final LockConfig lockConfig) {
        this.connectionFactory = connectionFactory;
        this.lockConfig = lockConfig;
    }

    @Override
    public <T> Mono<T> executeWithLock(final String identifier, final Supplier<Mono<T>> supplier) {
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        return Mono.defer(() -> Mono.usingWhen(
                connectionFactory.create(),
                conn -> doExecuteWithLock(conn, lockKey, timeoutSeconds, supplier),
                Connection::close
        )).contextWrite(MdcUtil.mdcToReactorContext());
    }

    @Override
    public <T> Flux<T> executeManyWithLock(final String identifier, final Supplier<Flux<T>> supplier) {
        final String lockKey = KeyGenerator.generate(lockConfig.getKey(), identifier);
        final int timeoutSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(lockConfig.getWaitTime());

        return Flux.defer(() -> Flux.usingWhen(
                connectionFactory.create(),
                conn -> doExecuteManyWithLock(conn, lockKey, timeoutSeconds, supplier),
                Connection::close
        )).contextWrite(MdcUtil.mdcToReactorContext());
    }

    private <T> Mono<T> doExecuteWithLock(
            final Connection connection,
            final String lockKey,
            final int timeoutSeconds,
            final Supplier<Mono<T>> supplier
    ) {
        return Mono.usingWhen(
                acquire(connection, lockKey, timeoutSeconds)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Failed to acquire lock for key: " + lockKey))),
                acquired -> supplier.get(),
                acquired -> release(connection, lockKey)
        );
    }

    private <T> Flux<T> doExecuteManyWithLock(
            final Connection connection,
            final String lockKey,
            final int timeoutSeconds,
            final Supplier<Flux<T>> supplier
    ) {
        return Flux.usingWhen(
                acquire(connection, lockKey, timeoutSeconds)
                        .filter(Boolean::booleanValue)
                        .switchIfEmpty(Mono.error(new IllegalStateException("Failed to acquire lock for key: " + lockKey))),
                acquired -> supplier.get(),
                acquired -> release(connection, lockKey)
        );
    }

    private Mono<Boolean> acquire(final Connection connection, final String lockKey, final int timeoutSeconds) {
        return Mono.from(connection.createStatement(GET_LOCK_STATEMENT)
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
        return Mono.from(connection.createStatement(RELEASE_LOCK_STATEMENT)
                        .bind(0, lockKey)
                        .execute())
                .flatMap(result -> Mono.from(result.map((row, meta) -> row.get(0))))
                .doOnSuccess(r -> log.info("Released lock for key: {}", lockKey))
                .doOnError(e -> log.error("Failed to release lock for key: {}", lockKey))
                .onErrorMap(e -> new IllegalStateException("Failed to release lock for key: " + lockKey))
                .then();
    }
}
