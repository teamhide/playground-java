package com.teamhide.playground.webfluxworld.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoDto {
    private final Long memberId;
    private final String orderId;
    private final Integer point;
}
