package com.teamhide.playground.annotationvalidator;

import com.teamhide.playground.annotationvalidator.validator.FieldValidator;
import com.teamhide.playground.annotationvalidator.validator.MaxValidator;
import com.teamhide.playground.annotationvalidator.validator.MinValidator;
import com.teamhide.playground.annotationvalidator.validator.NotBlankValidator;
import com.teamhide.playground.annotationvalidator.validator.NotNullValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

public class Validator {
    private static final List<FieldValidator> VALIDATORS = List.of(
            new NotNullValidator(),
            new NotBlankValidator(),
            new MinValidator(),
            new MaxValidator()
    );

    public static void validate(final Object target) {
        final Class<?> clazz = target.getClass();
        final Field[] fields = clazz.getDeclaredFields();

        for (final Field field : fields) {
            field.setAccessible(true);

            final Object value;
            try {
                value = field.get(target);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }

            for (Annotation annotation : field.getAnnotations()) {
                for (FieldValidator validator : VALIDATORS) {
                    if (validator.supports(annotation)) {
                        validator.validate(field, value, annotation);
                    }
                }
            }
        }
    }
}
