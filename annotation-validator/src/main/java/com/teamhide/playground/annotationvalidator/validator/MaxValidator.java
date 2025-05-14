package com.teamhide.playground.annotationvalidator.validator;

import com.teamhide.playground.annotationvalidator.ValidationException;
import com.teamhide.playground.annotationvalidator.annotation.Max;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MaxValidator implements FieldValidator {
    @Override
    public boolean supports(final Annotation annotation) {
        return annotation instanceof Max;
    }

    @Override
    public void validate(final Field field, final Object value, final Annotation annotation) {
        if (!(value instanceof Number number)) {
            throw new ValidationException("@Max can only be applied to numeric fields: " + field.getName());
        }

        long max = ((Max) annotation).value();
        if (number.longValue() > max) {
            throw new ValidationException(field.getName() + " must be <= " + max);
        }
    }
}
