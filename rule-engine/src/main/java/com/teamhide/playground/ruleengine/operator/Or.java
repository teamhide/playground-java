package com.teamhide.playground.ruleengine.operator;

import java.util.List;

public class Or implements Rule {
    private final List<Rule> rules;

    public Or(final List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean evaluate() {
        return rules.stream().anyMatch(rule -> rule.evaluate());
    }
}
