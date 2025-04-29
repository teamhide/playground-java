package com.teamhide.playground.ruleengine;

import com.teamhide.playground.ruleengine.operator.Rule;
import com.teamhide.playground.ruleengine.rules.AdminNameRule;
import com.teamhide.playground.ruleengine.rules.UserAgeRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class RulesTest {
    @Test
    @DisplayName("true && true는 true를 리턴한다")
    void testAndRule() {
        // Given
        final Rule rules = new UserAgeRule(25)
                .and(new AdminNameRule("admin"));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("true || false는 true를 리턴한다")
    void testOrRule() {
        // Given
        final Rule rules = new UserAgeRule(25)
                .or(new AdminNameRule("hide"));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("true && false는 false를 리턴한다")
    void testComplexRule_1() {
        // Given
        final Rule rules = new UserAgeRule(25)
                .and(new AdminNameRule("hide"));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isFalse();
    }

    @Test
    @DisplayName("true || false는 false를 리턴한다")
    void testComplexRule_2() {
        // Given
        final Rule rules = new UserAgeRule(25)
                .or(new AdminNameRule("hide"));

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }

    @Test
    @DisplayName("(true || false) && (false && false)는 false를 리턴한다")
    void testComplexRule_3() {
        // Given
        final Rule rules1 = new UserAgeRule(25)
                .or(new AdminNameRule("hide"));

        final Rule rules2 = new UserAgeRule(1)
                .and(new AdminNameRule("hide"));

        final Rule rules = rules1.and(rules2);

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isFalse();
    }

    @Test
    @DisplayName("(true || false) || (false && false)는 false를 리턴한다")
    void testComplexRule_4() {
        // Given
        final Rule rules1 = new UserAgeRule(25)
                .or(new AdminNameRule("hide"));

        final Rule rules2 = new UserAgeRule(1)
                .and(new AdminNameRule("hide"));

        final Rule rules = rules1.or(rules2);

        // When
        final boolean sut = rules.evaluate();

        // Then
        assertThat(sut).isTrue();
    }
}
