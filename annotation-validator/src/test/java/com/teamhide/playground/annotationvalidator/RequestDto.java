package com.teamhide.playground.annotationvalidator;

import com.teamhide.playground.annotationvalidator.annotation.Max;
import com.teamhide.playground.annotationvalidator.annotation.Min;
import com.teamhide.playground.annotationvalidator.annotation.NotBlank;
import com.teamhide.playground.annotationvalidator.annotation.NotNull;

public class RequestDto {
    @NotNull
    private final String id;

    @NotBlank
    private final String name;

    @Min(1)
    @Max(5)
    private final int quantity;

    public RequestDto(final String id, final String name, final int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
