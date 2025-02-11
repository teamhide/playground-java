package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.service.dto.BannerMetaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BannerMetaService {
    public static final String BANNER_KEY_PREFIX = "banner_meta";
    private final ReactiveRedisOperations<String, String> redisOperations;

    public Mono<List<BannerMetaDto>> findAllByBannerIds(final List<String> bannerIds) {
        return Flux.fromIterable(bannerIds)
                .map(this::makeKey)
                .collectList()
                .flatMap(redisKeys -> redisOperations.opsForValue().multiGet(redisKeys))
                .flatMapMany(values -> Flux.fromIterable(Objects.requireNonNull(values)))
                .zipWith(Flux.fromIterable(bannerIds))
                .map(tuple -> {
                    final String value = tuple.getT1();
                    final String bannerId = tuple.getT2();
                    return toBannerMetaDto(bannerId, value);
                })
                .collectList();
    }

    private String makeKey(final String bannerId) {
        return BANNER_KEY_PREFIX + ":" + bannerId;
    }

    private BannerMetaDto toBannerMetaDto(final String bannerId, final String value) {
        return new BannerMetaDto(bannerId, Integer.valueOf(value));
    }
}
