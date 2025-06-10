package com.teamhide.playground.distributedlock;

import javax.sql.DataSource;
import java.util.function.Supplier;

public class MySqlLockManager implements LockManager {
    private final DataSource dataSource;
    private final LockConfig config;

    public MySqlLockManager(final DataSource dataSource, final LockConfig config) {
        this.dataSource = dataSource;
        this.config = config;
    }

    @Override
    public <T> T executeWithLock(final String identifier, final Supplier<T> supplier) {
        return supplier.get();
    }
}
