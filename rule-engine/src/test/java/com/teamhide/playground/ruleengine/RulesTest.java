package com.teamhide.playground.ruleengine;

import com.teamhide.playground.ruleengine.operator.And;
import com.teamhide.playground.ruleengine.operator.Or;
import com.teamhide.playground.ruleengine.operator.Rule;
import com.teamhide.playground.ruleengine.rules.AdminNameRule;
import com.teamhide.playground.ruleengine.rules.UserAgeRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RulesTest {
    @Test
    @DisplayName("true && true는 true를 리턴한다")
    void testAndRule() {
        // Given
        final Rule rule1 = new UserAgeRule(25);
        final Rule rule2 = new AdminNameRule("admin");
        final And rules = new And(List.of(rule1, rule2));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("true || false는 true를 리턴한다")
    void testOrRule() {
        // Given
        final Rule rule1 = new UserAgeRule(25);
        final Rule rule2 = new AdminNameRule("hide");
        final Or rules = new Or(List.of(rule1, rule2));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("true && false는 false를 리턴한다")
    void testComplexRule_1() {
        // Given
        final Rule trueRule1 = new UserAgeRule(25);
        final Rule falseRule1 = new AdminNameRule("hide");
        final And rules = new And(List.of(trueRule1, falseRule1));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isFalse();
    }

    @Test
    @DisplayName("true || false는 false를 리턴한다")
    void testComplexRule_2() {
        // Given
        final Rule trueRule1 = new UserAgeRule(25);
        final Rule falseRule1 = new AdminNameRule("hide");
        final Or rules = new Or(List.of(trueRule1, falseRule1));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("(true || false) && (false && false)는 false를 리턴한다")
    void testComplexRule_3() {
        // Given
        final Rule trueRule1 = new UserAgeRule(25);
        final Rule falseRule1 = new AdminNameRule("hide");
        final Or rules1 = new Or(List.of(trueRule1, falseRule1));

        final Rule falseRule2 = new UserAgeRule(1);
        final Rule falseRule3 = new AdminNameRule("hide");
        final And rules2 = new And(List.of(falseRule2, falseRule3));

        final And rules = new And(List.of(rules1, rules2));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isFalse();
    }

    @Test
    @DisplayName("(true || false) || (false && false)는 false를 리턴한다")
    void testComplexRule_4() {
        // Given
        final Rule trueRule1 = new UserAgeRule(25);
        final Rule falseRule1 = new AdminNameRule("hide");
        final Or rules1 = new Or(List.of(trueRule1, falseRule1));

        final Rule falseRule2 = new UserAgeRule(1);
        final Rule falseRule3 = new AdminNameRule("hide");
        final And rules2 = new And(List.of(falseRule2, falseRule3));

        final Or rules = new Or(List.of(rules1, rules2));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }
}
