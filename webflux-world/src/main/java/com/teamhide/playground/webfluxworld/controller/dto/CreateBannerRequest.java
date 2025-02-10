package com.teamhide.playground.webfluxworld.controller.dto;

import com.teamhide.playground.webfluxworld.service.dto.CreateBannerRequestDto;
import lombok.Getter;

@Getter
public class CreateBannerRequest {
    private String imageUrl;
    private String title;
    private String subTitle;

    public CreateBannerRequestDto toRequestDto() {
        return new CreateBannerRequestDto(imageUrl, title, subTitle);
    }
}
