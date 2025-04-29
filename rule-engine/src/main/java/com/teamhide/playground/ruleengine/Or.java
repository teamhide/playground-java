package com.teamhide.playground.ruleengine;

import java.util.ArrayList;
import java.util.List;

public class Or implements Rule {
    private final List<Rule> rules;

    public Or(final List<Rule> rules) {
        this.rules = rules;
    }

    @Override
    public boolean evaluate() {
        return rules.stream().anyMatch(Rule::evaluate);
    }

    @Override
    public Rule or(final Rule other) {
        final List<Rule> newRules = new ArrayList<>(rules);
        newRules.add(other);
        return new Or(newRules);
    }
}
