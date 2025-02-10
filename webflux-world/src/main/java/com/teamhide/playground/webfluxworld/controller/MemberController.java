package com.teamhide.playground.webfluxworld.controller;

import com.teamhide.playground.webfluxworld.controller.dto.RegisterMemberRequest;
import com.teamhide.playground.webfluxworld.repository.rdb.Member;
import com.teamhide.playground.webfluxworld.service.MemberService;
import com.teamhide.playground.webfluxworld.service.dto.MemberDto;
import com.teamhide.playground.webfluxworld.service.dto.RegisterMemberRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("")
    public Flux<Member> getAllMembers() {
        return memberService.getAllMember();
    }

    @PostMapping("")
    public Mono<MemberDto> registerMember(@RequestBody @Valid final RegisterMemberRequest request) {
        final RegisterMemberRequestDto requestDto = RegisterMemberRequestDto.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password1(request.getPassword1())
                .password2(request.getPassword2())
                .build();
        return memberService.register(requestDto);
    }
}
