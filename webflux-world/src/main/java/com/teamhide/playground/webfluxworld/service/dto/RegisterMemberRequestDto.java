package com.teamhide.playground.webfluxworld.service.dto;

import lombok.Builder;

@Builder
public record RegisterMemberRequestDto(
    String email,
    String nickname,
    String password1,
    String password2
) {}
