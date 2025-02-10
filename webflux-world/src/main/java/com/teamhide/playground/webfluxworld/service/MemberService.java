package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.repository.Member;
import com.teamhide.playground.webfluxworld.repository.MemberRepository;
import com.teamhide.playground.webfluxworld.service.dto.MemberDto;
import com.teamhide.playground.webfluxworld.service.dto.RegisterMemberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {
    private final MemberRepository memberRepository;

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
}
