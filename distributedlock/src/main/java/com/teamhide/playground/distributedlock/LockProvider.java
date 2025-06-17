package com.teamhide.playground.distributedlock;

import com.teamhide.playground.distributedlock.config.LockConfig;

public interface LockProvider {
    String type();
    LockManager create(LockConfig config);
}
