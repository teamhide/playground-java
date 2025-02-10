package com.teamhide.playground.webfluxworld.controller;

import com.teamhide.playground.webfluxworld.controller.dto.CreateBannerRequest;
import com.teamhide.playground.webfluxworld.service.BannerService;
import com.teamhide.playground.webfluxworld.service.dto.BannerDto;
import com.teamhide.playground.webfluxworld.service.dto.CreateBannerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("")
    public Mono<List<BannerDto>> getBanners() {
        return bannerService.getBanners();
    }

    @PostMapping("")
    public Mono<BannerDto> createBanner(@RequestBody final CreateBannerRequest request) {
        final CreateBannerRequestDto requestDto = request.toRequestDto();
        return bannerService.createBanner(requestDto);
    }
}
