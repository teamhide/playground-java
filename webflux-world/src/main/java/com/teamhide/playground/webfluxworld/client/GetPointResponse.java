package com.teamhide.playground.webfluxworld.client;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
