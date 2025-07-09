package com.teamhide.playground.gatekeeper;

public class DistributedLockException extends RuntimeException {
    public DistributedLockException(final String message) {
        super(message);
    }

    public DistributedLockException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
