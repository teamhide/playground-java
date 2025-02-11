package com.teamhide.playground.webfluxworld.client;

import lombok.Getter;

@Getter
public class GetPointResponse {
    private Long memberId;
    private Integer point;

    @Override
    public String toString() {
        return "GetPointResponse{" +
                "memberId=" + memberId +
                ", point=" + point +
                '}';
    }
}
