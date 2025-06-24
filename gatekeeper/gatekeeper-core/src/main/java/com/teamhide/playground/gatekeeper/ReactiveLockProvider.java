package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;

public interface ReactiveLockProvider {
    String type();
    ReactiveLockManager create(LockConfig config);
}
