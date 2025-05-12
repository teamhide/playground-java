package com.teamhide.playground.retry;

public class RetryException extends RuntimeException {
    public RetryException(final String message) {
        super(message);
    }

    public RetryException() {
    }

    public RetryException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
