package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.service.dto.BannerMetaDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BannerMetaServiceTest {
    @Mock
    private ReactiveRedisOperations<String, String> redisOperations;

    @Mock
    private ReactiveValueOperations<String, String> valueOperations;

    @InjectMocks
    private BannerMetaService bannerMetaService;

    @Test
    void testFindAllByBannerIds() {
        // Given
        final List<String> bannerIds = List.of("1", "2", "3");
        final List<String> clickCounts = List.of("4", "5", "6");
        when(valueOperations.multiGet(any())).thenReturn(Mono.just(clickCounts));
        when(redisOperations.opsForValue()).thenReturn(valueOperations);

        // When
        final Mono<List<BannerMetaDto>> result = bannerMetaService.findAllByBannerIds(bannerIds);

        // Then
        StepVerifier.create(result)
                .assertNext(bannerMetaDtos -> {
                    assertThat(bannerMetaDtos.get(0))
                            .extracting(BannerMetaDto::bannerId, BannerMetaDto::clickCount)
                            .containsExactly("1", 4);

                    assertThat(bannerMetaDtos.get(1))
                            .extracting(BannerMetaDto::bannerId, BannerMetaDto::clickCount)
                            .containsExactly("2", 5);

                    assertThat(bannerMetaDtos.get(2))
                            .extracting(BannerMetaDto::bannerId, BannerMetaDto::clickCount)
                            .containsExactly("3", 6);
                })
                .verifyComplete();
    }
}
