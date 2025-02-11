package com.teamhide.playground.webfluxworld.repository.redis;

import com.teamhide.playground.webfluxworld.repository.rdb.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberRedis {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberRedis from(final Member member) {
        return MemberRedis.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
