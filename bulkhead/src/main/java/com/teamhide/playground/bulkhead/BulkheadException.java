package com.teamhide.playground.bulkhead;

public class BulkheadException extends RuntimeException {
    public BulkheadException(final String message) {
        super(message);
    }

    public BulkheadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
