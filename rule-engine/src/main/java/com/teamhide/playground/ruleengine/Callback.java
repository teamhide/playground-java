package com.teamhide.playground.ruleengine;

import java.util.function.Consumer;

public class Callback implements Rule {
    private final Rule delegate;
    private Consumer<Void> onSuccess = v -> {};
    private Consumer<Void> onFailure = v -> {};

    public Callback(final Rule delegate) {
        this.delegate = delegate;
    }

    public Callback onSuccess(final Runnable callback) {
        this.onSuccess = v -> callback.run();
        return this;
    }

    public Callback onFailure(final Runnable callback) {
        this.onFailure = v -> callback.run();
        return this;
    }

    @Override
    public boolean evaluate() {
        final boolean result = delegate.evaluate();
        if (result) {
            onSuccess.accept(null);
        } else {
            onFailure.accept(null);
        }
        return result;
    }
}
