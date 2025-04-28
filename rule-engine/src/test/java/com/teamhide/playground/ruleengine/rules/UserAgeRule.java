package com.teamhide.playground.ruleengine.rules;

import com.teamhide.playground.ruleengine.operator.Rule;

public class UserAgeRule implements Rule {
    private final int age;

    public UserAgeRule(final int age) {
        this.age = age;
    }

    @Override
    public boolean evaluate() {
        return age >= 20;
    }
}
