package com.teamhide.playground.webfluxworld.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Slf4j
@Repository
public class MemberRedisRepository {
    private static final String KEY_PREFIX = "user";
    private final ReactiveRedisOperations<String, String> redisOperations;
    private final ObjectMapper objectMapper;

    public Mono<MemberRedis> findById(final Long memberId) {
        final String key = makeKey(memberId);
        return redisOperations.opsForValue().get(key)
                .flatMap(value -> {
                    try {
                        return Mono.just(objectMapper.readValue(value, MemberRedis.class));
                    } catch (JsonProcessingException e) {
                        log.error("MemberRedisRepository#findById | error: {}", e.getMessage());
                        return Mono.empty();
                    }
                });
    }

    public Mono<MemberRedis> save(final MemberRedis memberRedis) {
        final String key = makeKey(memberRedis.getId());
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(memberRedis))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(member ->
                        redisOperations.opsForValue().set(key, member)
                                .thenReturn(memberRedis)
                )
                .onErrorResume(e -> {
                    log.error("MemberRedisRepository#save | error: {}", e.getMessage());
                    return Mono.error(e);
                });
    }

    private String makeKey(final Long memberId) {
        return KEY_PREFIX + ":" + memberId;
    }
}
