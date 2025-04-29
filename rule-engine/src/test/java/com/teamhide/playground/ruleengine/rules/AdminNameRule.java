package com.teamhide.playground.ruleengine.rules;

import com.teamhide.playground.ruleengine.Rule;

public class AdminNameRule implements Rule {
    private final String name;

    public AdminNameRule(final String name) {
        this.name = name;
    }

    @Override
    public boolean evaluate() {
        return name.equals("admin");
    }
}
