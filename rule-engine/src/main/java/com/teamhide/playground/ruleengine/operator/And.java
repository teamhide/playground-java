package com.teamhide.playground.ruleengine.operator;

import java.util.List;

public class And implements Rule {
    private final List<Rule> rules;

    public And(final List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean evaluate() {
        return rules.stream().allMatch(Rule::evaluate);
    }
}
