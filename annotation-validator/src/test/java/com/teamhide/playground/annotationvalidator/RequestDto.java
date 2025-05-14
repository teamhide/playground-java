package com.teamhide.playground.annotationvalidator;

import com.teamhide.playground.annotationvalidator.annotation.NotBlank;
import com.teamhide.playground.annotationvalidator.annotation.NotNull;

public class RequestDto {
    @NotNull
    private final String id;

    @NotBlank
    private final String name;

    public RequestDto(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
