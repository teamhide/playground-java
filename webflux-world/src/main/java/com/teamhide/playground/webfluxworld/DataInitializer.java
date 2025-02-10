package com.teamhide.playground.webfluxworld;

import com.teamhide.playground.webfluxworld.repository.mongo.Banner;
import com.teamhide.playground.webfluxworld.repository.mongo.BannerRepository;
import com.teamhide.playground.webfluxworld.repository.rdb.Member;
import com.teamhide.playground.webfluxworld.repository.rdb.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final MemberRepository memberRepository;
    private final BannerRepository bannerRepository;

    @PostConstruct
    public void init() {
        log.info("[*] Initializing data start");
        Mono.when(memberRepository.deleteAll(), bannerRepository.deleteAll()).block();

        final Member member1 = Member.of("h@id.e", "hide", "a", "a");
        final Member member2 = Member.of("test@id.e", "test", "a", "a");
        memberRepository.saveAll(List.of(member1, member2)).subscribe();

        final Banner banner1 = Banner.of("http://h.ide/1.jpg", "title1", "sub1");
        final Banner banner2 = Banner.of("http://h.ide/2.jpg", "title2", "sub2");
        bannerRepository.saveAll(List.of(banner1, banner2)).subscribe();

        log.info("[*] Initializing data end");
    }
}
