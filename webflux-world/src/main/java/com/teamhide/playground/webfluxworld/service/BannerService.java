package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.repository.mongo.Banner;
import com.teamhide.playground.webfluxworld.repository.mongo.BannerRepository;
import com.teamhide.playground.webfluxworld.service.dto.BannerDto;
import com.teamhide.playground.webfluxworld.service.dto.BannerMetaDto;
import com.teamhide.playground.webfluxworld.service.dto.CreateBannerRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerRepository bannerRepository;
    private final BannerMetaService bannerMetaService;

    public Mono<List<BannerDto>> getBanners() {
        return bannerRepository.findAll()
                .collectList()
                .flatMap(banners -> {
                    final List<String> bannerIds = banners.stream()
                            .map(Banner::getId)
                            .toList();
                    return bannerMetaService.findAllByBannerIds(bannerIds)
                            .map(bannerMetaDtos -> toBannerDtos(banners, bannerMetaDtos));
                });
    }

    public Mono<BannerDto> createBanner(final CreateBannerRequestDto requestDto) {
        final Banner banner = Banner.of(requestDto.imageUrl(), requestDto.title(), requestDto.subTitle());
        return bannerRepository.save(banner).map(BannerDto::from);
    }

    private List<BannerDto> toBannerDtos(final List<Banner> banners, final List<BannerMetaDto> bannerMetaDtos) {
        final Map<String, Integer> bannerIdToClickCount = bannerMetaDtos.stream()
                .collect(Collectors.toMap(BannerMetaDto::bannerId, BannerMetaDto::clickCount));

        return banners.stream()
                .map(banner -> {
                    final Integer clickCount = bannerIdToClickCount.getOrDefault(banner.getId(), 0);
                    return BannerDto.from(banner, clickCount);
                })
                .toList();
    }
}
