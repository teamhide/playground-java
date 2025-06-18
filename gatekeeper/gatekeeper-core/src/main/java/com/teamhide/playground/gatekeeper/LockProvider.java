package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfig;

public interface LockProvider {
    String type();
    LockManager create(LockConfig config);
}
