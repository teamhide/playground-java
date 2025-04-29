package com.teamhide.playground.ruleengine.operator;

import java.util.List;

public interface Rule {
    boolean evaluate();

    default Rule and(final Rule other) {
        return new And(List.of(this, other));
    }

    default Rule or(final Rule other) {
        return new Or(List.of(this, other));
    }
}
