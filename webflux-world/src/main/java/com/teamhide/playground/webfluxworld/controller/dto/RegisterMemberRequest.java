package com.teamhide.playground.webfluxworld.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RegisterMemberRequest {
    @NotNull
    private String email;
    @NotNull
    private String nickname;
    @NotNull
    private String password1;
    @NotNull
    private String password2;
}
