package com.teamhide.playground.webfluxworld.service.dto;

public record CreateBannerRequestDto(
        String imageUrl,
        String title,
        String subTitle
) {
}
