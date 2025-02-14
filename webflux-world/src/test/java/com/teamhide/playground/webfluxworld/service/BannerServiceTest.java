package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.repository.mongo.Banner;
import com.teamhide.playground.webfluxworld.repository.mongo.BannerRepository;
import com.teamhide.playground.webfluxworld.service.dto.BannerDto;
import com.teamhide.playground.webfluxworld.service.dto.CreateBannerRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BannerServiceTest {
    @Mock
    private BannerRepository bannerRepository;

    @Mock
    private BannerMetaService bannerMetaService;

    @InjectMocks
    private BannerService bannerService;

    @Test
    void testGetBanners() {
        // Given
        final List<Banner> banners = List.of(
                Banner.of("url1", "title1", "subTitle1"),
                Banner.of("url2", "title2", "subTitle2")
        );
        when(bannerRepository.findAll()).thenReturn(Flux.fromIterable(banners));

        // When
        Mono<List<BannerDto>> result = bannerService.getBanners();

        // Then
        StepVerifier.create(result)
                .assertNext(bannerDtos -> {
                    assertThat(bannerDtos).hasSize(2);
                    assertThat(bannerDtos.get(0).title()).isEqualTo("title1");
                    assertThat(bannerDtos.get(1).title()).isEqualTo("title2");
                })
                .verifyComplete();
        verify(bannerRepository).findAll();
    }

    @Test
    void testCreateBanner() {
        // Given
        final CreateBannerRequestDto requestDto = new CreateBannerRequestDto("url1", "title1", "subTitle1");
        final Banner savedBanner = Banner.of("url1", "title1", "subTitle1");

        when(bannerRepository.save(any(Banner.class))).thenReturn(Mono.just(savedBanner));

        // When
        final Mono<BannerDto> result = bannerService.createBanner(requestDto);

        // Then
        StepVerifier.create(result)
                .assertNext(bannerDto -> {
                    assertThat(bannerDto.imageUrl()).isEqualTo(savedBanner.getImageUrl());
                    assertThat(bannerDto.title()).isEqualTo(savedBanner.getTitle());
                    assertThat(bannerDto.subTitle()).isEqualTo(savedBanner.getSubTitle());
                })
                .verifyComplete();

        // Verify repository interaction
        verify(bannerRepository).save(any(Banner.class));
    }
}
