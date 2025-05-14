package com.teamhide.playground.annotationvalidator.validator;

import com.teamhide.playground.annotationvalidator.ValidationException;
import com.teamhide.playground.annotationvalidator.annotation.Min;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class MinValidator implements FieldValidator {
    @Override
    public boolean supports(final Annotation annotation) {
        return annotation instanceof Min;
    }

    @Override
    public void validate(final Field field, final Object value, final Annotation annotation) {
        if (!(value instanceof Number number)) {
            throw new ValidationException("@Min can only be applied to numbers: " + field.getName());
        }
        long min = ((Min) annotation).value();
        if (number.longValue() < min) {
            throw new ValidationException(field.getName() + " must be >= " + min);
        }
    }
}
