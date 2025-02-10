package com.teamhide.playground.webfluxworld.service.dto;

import com.teamhide.playground.webfluxworld.repository.rdb.Member;

import java.time.LocalDateTime;

public record MemberDto(
        Long id,
        String email,
        String nickname,
        LocalDateTime createdAt
) {
    public static MemberDto from(final Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getCreatedAt());
    }
}
