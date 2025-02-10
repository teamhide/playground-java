package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.repository.mongo.Banner;
import com.teamhide.playground.webfluxworld.repository.mongo.BannerRepository;
import com.teamhide.playground.webfluxworld.service.dto.BannerDto;
import com.teamhide.playground.webfluxworld.service.dto.CreateBannerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;

    public Mono<List<BannerDto>> getBanners() {
        return bannerRepository.findAll().map(BannerDto::from).collectList();
    }

    public Mono<BannerDto> createBanner(final CreateBannerRequestDto requestDto) {
        final Banner banner = Banner.of(requestDto.imageUrl(), requestDto.title(), requestDto.subTitle());
        return bannerRepository.save(banner).map(BannerDto::from);
    }
}
