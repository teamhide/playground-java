package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.client.OrderClient;
import com.teamhide.playground.webfluxworld.client.PointClient;
import com.teamhide.playground.webfluxworld.repository.rdb.Member;
import com.teamhide.playground.webfluxworld.repository.rdb.MemberRepository;
import com.teamhide.playground.webfluxworld.repository.redis.MemberRedis;
import com.teamhide.playground.webfluxworld.repository.redis.MemberRedisRepository;
import com.teamhide.playground.webfluxworld.service.dto.MemberDto;
import com.teamhide.playground.webfluxworld.service.dto.MemberInfoDto;
import com.teamhide.playground.webfluxworld.service.dto.RegisterMemberRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRedisRepository memberRedisRepository;
    private final OrderClient orderClient;
    private final PointClient pointClient;

    public Flux<Member> getAllMember() {
        return memberRepository.findAll();
    }

    public Mono<MemberDto> register(final RegisterMemberRequestDto requestDto) {
        final Member member = Member.of(
                requestDto.email(),
                requestDto.nickname(),
                requestDto.password1(),
                requestDto.password2()
        );
        return memberRepository.save(member).map(MemberDto::from);
    }

    public Mono<MemberDto> getMember(final Long memberId) {
        return memberRedisRepository.findById(memberId)
                .doOnError(e -> log.error("getMember | error: {}", e))
                .onErrorComplete()
                .flatMap(memberRedis -> Mono.just(MemberDto.from(memberRedis)))
                .switchIfEmpty(Mono.defer(() -> getMemberFromDb(memberId)));
    }

    public Mono<MemberInfoDto> getMemberInfo(final Long memberId) {
        return memberRepository.findById(memberId)
                .flatMap(member -> Mono.zip(
                        orderClient.getOrder(),
                        pointClient.getPoint()
                ).map(tuple ->
                        MemberInfoDto.builder()
                                .memberId(member.getId())
                                .orderId(tuple.getT1().getOrderId())
                                .point(tuple.getT2().getPoint())
                                .build()))
                .switchIfEmpty(Mono.error(new RuntimeException()));
    }

    private Mono<MemberDto> getMemberFromDb(final Long memberId) {
        return memberRepository.findById(memberId)
                .doOnNext((member) -> log.info("getMember | member is empty. memberId: {}", memberId))
                .map(MemberRedis::from)
                .flatMap(memberRedisRepository::save)
                .map(MemberDto::from);
    }
}
