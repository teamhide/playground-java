package com.teamhide.playground.annotationvalidator;

import com.teamhide.playground.annotationvalidator.annotation.Max;
import com.teamhide.playground.annotationvalidator.annotation.Min;
import com.teamhide.playground.annotationvalidator.annotation.NotBlank;
import com.teamhide.playground.annotationvalidator.annotation.NotNull;

import java.lang.reflect.Field;

public class Validator {
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

            if (field.isAnnotationPresent(NotNull.class) && value == null) {
                throw new ValidationException(field.getName() + " must not be null");
            }

            if (field.isAnnotationPresent(NotBlank.class)) {
                if (value instanceof String str) {
                    if (str.isBlank()) {
                        throw new ValidationException(field.getName() + " must not be blank");
                    }
                } else {
                    throw new ValidationException("@NotBlank can only be applied to String fields: " + field.getName());
                }
            }

            if (field.isAnnotationPresent(Min.class)) {
                final Min min = field.getAnnotation(Min.class);
                if (value instanceof Number number) {
                    if (number.longValue() < min.value()) {
                        throw new ValidationException(String.format("%s must be >= %d", field.getName(), min.value()));
                    }
                } else {
                    throw new ValidationException("@Min can only be applied to numeric fields: " + field.getName());
                }
            }

            if (field.isAnnotationPresent(Max.class)) {
                final Max max = field.getAnnotation(Max.class);
                if (value instanceof Number number) {
                    if (number.longValue() > max.value()) {
                        throw new ValidationException(String.format("%s must be <= %d", field.getName(), max.value()));
                    }
                } else {
                    throw new ValidationException("@Max can only be applied to numeric fields: " + field.getName());
                }
            }
        }
    }
}
