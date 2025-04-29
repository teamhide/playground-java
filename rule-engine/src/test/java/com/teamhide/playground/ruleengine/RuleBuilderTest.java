package com.teamhide.playground.ruleengine;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RuleBuilderTest {
    @Test
    @DisplayName("true && false인 경우 false를 리턴한다")
    void testRuleBuilder() {
        // Given
        final Runnable onSuccess = mock(Runnable.class);
        final Runnable onFailure = mock(Runnable.class);
        final Rule rule = RuleBuilder.when(() -> true)
                .and(() -> false)
                .onSuccess(onSuccess)
                .onFailure(onFailure)
                .build();

        // When
        final boolean sut = rule.evaluate();

        // Then
        assertThat(sut).isFalse();
        verify(onSuccess, times(0)).run();
        verify(onFailure, times(1)).run();
    }
}
