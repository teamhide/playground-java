package com.teamhide.playground.annotationvalidator.validator;

import com.teamhide.playground.annotationvalidator.ValidationException;
import com.teamhide.playground.annotationvalidator.annotation.NotBlank;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class NotBlankValidator implements FieldValidator {
    @Override
    public boolean supports(final Annotation annotation) {
        return annotation instanceof NotBlank;
    }

    @Override
    public void validate(final Field field, final Object value, final Annotation annotation) {
        if (!(value instanceof String str)) {
            throw new ValidationException("@NotBlank can only be applied to String fields: " + field.getName());
        }

        if (str.isBlank()) {
            throw new ValidationException(field.getName() + " must not be blank");
        }
    }
}
