package com.teamhide.playground.distributedlock;

import javax.sql.DataSource;

public class MySqlLockProvider implements LockProvider {
    private final DataSource dataSource;

    public MySqlLockProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String type() {
        return "mysql";
    }

    @Override
    public LockManager create(final LockConfig config) {
        return new MySqlLockManager(dataSource, config);
    }
}
