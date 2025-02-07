package com.teamhide.playground.webfluxworld.repository;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "member")
public class Member {
    @Id
    private Long id;
    private String email;
    private String nickname;
    private String password;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Member of(final String email, final String nickname, final String password1, final String password2) {
        Member member = new Member();
        member.email = email;
        member.nickname = nickname;
        member.password = password1;
        return member;
    }
}
