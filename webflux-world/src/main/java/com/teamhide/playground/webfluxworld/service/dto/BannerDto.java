package com.teamhide.playground.webfluxworld.service.dto;

import com.teamhide.playground.webfluxworld.repository.mongo.Banner;

import java.time.LocalDateTime;

public record BannerDto(
        String bannerId,
        String imageUrl,
        String title,
        String subTitle,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BannerDto from(final Banner banner) {
        return new BannerDto(
                banner.getId(),
                banner.getImageUrl(),
                banner.getTitle(),
                banner.getSubTitle(),
                banner.getCreatedAt(),
                banner.getUpdatedAt()
        );
    }
}
