package com.teamhide.playground.ruleengine;

import java.util.ArrayList;
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

    @Override
    public Rule and(final Rule other) {
        final List<Rule> newRules = new ArrayList<>(rules);
        newRules.add(other);
        return new And(newRules);
    }

}
