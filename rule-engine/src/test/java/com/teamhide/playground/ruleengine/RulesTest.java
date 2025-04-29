package com.teamhide.playground.ruleengine;

import com.teamhide.playground.ruleengine.rules.AdminNameRule;
import com.teamhide.playground.ruleengine.rules.UserAgeRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    @DisplayName("특정 룰을 실행하고 성공하면 onSuccess에 등록된 콜백을 실행한다")
    void testCallbackOnSuccess() {
        // Given
        final UserAgeRule userAgeRule = new UserAgeRule(25);
        final Runnable onSuccess = mock(Runnable.class);
        final Runnable onFailure = mock(Runnable.class);
        final Rule ruleWithCallback = new Callback(userAgeRule)
                .onSuccess(onSuccess)
                .onFailure(onFailure);

        // When
        ruleWithCallback.evaluate();

        // Then
        verify(onSuccess, times(1)).run();
        verify(onFailure, times(0)).run();
    }

    @Test
    @DisplayName("특정 룰을 실행하고 실패하면 onFailure에 등록된 콜백을 실행한다")
    void testCallbackOnFailure() {
        // Given
        final UserAgeRule userAgeRule = new UserAgeRule(1);
        final Runnable onSuccess = mock(Runnable.class);
        final Runnable onFailure = mock(Runnable.class);
        final Rule ruleWithCallback = new Callback(userAgeRule)
                .onSuccess(onSuccess)
                .onFailure(onFailure);

        // When
        ruleWithCallback.evaluate();

        // Then
        verify(onSuccess, times(0)).run();
        verify(onFailure, times(1)).run();
    }
}
