package com.teamhide.playground.annotationvalidator.validator;

import com.teamhide.playground.annotationvalidator.ValidationException;
import com.teamhide.playground.annotationvalidator.annotation.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class NotNullValidator implements FieldValidator {
    @Override
    public boolean supports(final Annotation annotation) {
        return annotation instanceof NotNull;
    }

    @Override
    public void validate(final Field field, final Object value, final Annotation annotation) {
        if (value == null) {
            throw new ValidationException(field.getName() + " must not be null");
        }
    }
}
