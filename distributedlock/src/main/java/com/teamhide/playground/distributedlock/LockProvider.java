package com.teamhide.playground.distributedlock;

public interface LockProvider {
    String type();
    LockManager create(LockConfig config);
}
