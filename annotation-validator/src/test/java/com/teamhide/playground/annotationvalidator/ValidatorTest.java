package com.teamhide.playground.annotationvalidator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidatorTest {
    @Test
    void testNotNull() {
        // Given
        final RequestDto requestDto = new RequestDto(null, "hide");

        // When, Then
        final ValidationException exception = assertThrows(ValidationException.class, () -> Validator.validate(requestDto));
        assertThat(exception.getMessage()).contains("must not be null");
    }

    @Test
    void testNotBlank() {
        // Given
        final RequestDto requestDto = new RequestDto("id", "");

        // When, Then
        final ValidationException exception = assertThrows(ValidationException.class, () -> Validator.validate(requestDto));
        assertThat(exception.getMessage()).contains("must not be blank");
    }
}
