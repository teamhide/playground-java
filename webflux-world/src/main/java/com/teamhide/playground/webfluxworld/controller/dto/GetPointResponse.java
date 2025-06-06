package com.teamhide.playground.webfluxworld.controller.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetPointResponse {
    private final Long memberId;
    private final Integer point;
}
