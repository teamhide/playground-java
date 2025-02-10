package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.repository.Member;
import com.teamhide.playground.webfluxworld.repository.MemberRepository;
import com.teamhide.playground.webfluxworld.service.dto.MemberDto;
import com.teamhide.playground.webfluxworld.service.dto.RegisterMemberRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void testGetAllMember() {
        // Given
        final List<Member> members = List.of(
                Member.of("user1@example.com", "user1", "pass1", "pass1"),
                Member.of("user2@example.com", "user2", "pass2", "pass2")
        );
        when(memberRepository.findAll()).thenReturn(Flux.fromIterable(members));

        // When
        StepVerifier.create(memberService.getAllMember())
                .expectNext(members.get(0))
                .expectNext(members.get(1))
                .expectComplete()
                .verify();

        // Then
        verify(memberRepository).findAll();
    }

    @Test
    void testRegister() {
        // Given
        final RegisterMemberRequestDto requestDto = new RegisterMemberRequestDto(
                "user@example.com",
                "user",
                "password1",
                "password1"
        );
        final Member member = Member.of(requestDto.email(), requestDto.nickname(), requestDto.password1(), requestDto.password2());
        final Member savedMember = Member.of("user@example.com", "user", "password1", "password1");
        when(memberRepository.save(any(Member.class))).thenReturn(Mono.just(savedMember));

        // When
        final Mono<MemberDto> result = memberService.register(requestDto);

        // Then
        StepVerifier.create(result)
                .assertNext(memberDto -> {
                    assertThat(memberDto.email()).isEqualTo(savedMember.getEmail());
                    assertThat(memberDto.nickname()).isEqualTo(savedMember.getNickname());
                })
                .verifyComplete();
        verify(memberRepository).save(any(Member.class));
    }
}
