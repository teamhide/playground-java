package com.teamhide.playground.gatekeeper.mysql.reactive;

import com.teamhide.playground.gatekeeper.ReactiveLockManager;
import com.teamhide.playground.gatekeeper.ReactiveLockProvider;
import com.teamhide.playground.gatekeeper.config.LockConfig;
import io.r2dbc.spi.ConnectionFactory;

public class ReactiveMySqlLockProvider implements ReactiveLockProvider {
    private final ConnectionFactory connectionFactory;

    public ReactiveMySqlLockProvider(final ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public String type() {
        return "mysql-reactive";
    }

    @Override
    public ReactiveLockManager create(final LockConfig config) {
        return new ReactiveMySqlLockManager(connectionFactory, config);
    }
}
