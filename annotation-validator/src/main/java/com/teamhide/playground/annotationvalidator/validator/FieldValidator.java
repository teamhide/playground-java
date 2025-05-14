package com.teamhide.playground.annotationvalidator.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface FieldValidator {
    boolean supports(Annotation annotation);
    void validate(Field field, Object value, Annotation annotation);
}
