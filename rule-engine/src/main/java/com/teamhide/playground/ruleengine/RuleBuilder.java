package com.teamhide.playground.ruleengine;

public class RuleBuilder {
    private Rule current;
    private Runnable onSuccess = () -> {};
    private Runnable onFailure = () -> {};

    private RuleBuilder(final Rule rule) {
        this.current = rule;
    }

    public static RuleBuilder when(final Rule rule) {
        return new RuleBuilder(rule);
    }

    public RuleBuilder and(final Rule other) {
        this.current = current.and(other);
        return this;
    }

    public RuleBuilder or(final Rule other) {
        this.current = current.or(other);
        return this;
    }

    public RuleBuilder onSuccess(final Runnable onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    public RuleBuilder onFailure(final Runnable onFailure) {
        this.onFailure = onFailure;
        return this;
    }

    public Rule build() {
        return new Callback(current)
                .onSuccess(onSuccess)
                .onFailure(onFailure);
    }
}
