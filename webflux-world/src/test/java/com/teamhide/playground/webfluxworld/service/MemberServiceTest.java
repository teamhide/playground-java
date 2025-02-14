package com.teamhide.playground.webfluxworld.service;

import com.teamhide.playground.webfluxworld.client.OrderClient;
import com.teamhide.playground.webfluxworld.client.PointClient;
import com.teamhide.playground.webfluxworld.client.dto.GetOrderResponse;
import com.teamhide.playground.webfluxworld.client.dto.GetPointResponse;
import com.teamhide.playground.webfluxworld.repository.rdb.Member;
import com.teamhide.playground.webfluxworld.repository.rdb.MemberRepository;
import com.teamhide.playground.webfluxworld.repository.redis.MemberRedis;
import com.teamhide.playground.webfluxworld.repository.redis.MemberRedisRepository;
import com.teamhide.playground.webfluxworld.service.dto.MemberDto;
import com.teamhide.playground.webfluxworld.service.dto.MemberInfoDto;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberRedisRepository memberRedisRepository;

    @Mock
    private OrderClient orderClient;

    @Mock
    private PointClient pointClient;

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
        final Flux<Member> sut = memberService.getAllMember();

        // Then
        StepVerifier.create(sut)
                .expectNext(members.get(0))
                .expectNext(members.get(1))
                .expectComplete()
                .verify();

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
        final Mono<MemberDto> sut = memberService.register(requestDto);

        // Then
        StepVerifier.create(sut)
                .assertNext(memberDto -> {
                    assertThat(memberDto.email()).isEqualTo(savedMember.getEmail());
                    assertThat(memberDto.nickname()).isEqualTo(savedMember.getNickname());
                })
                .verifyComplete();

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testGetMember() {
        // Given
        final Long memberId = 1L;
        final Member member = Member.of("h@.id.e", "hide", "a", "a");
        final MemberRedis memberRedis = MemberRedis.from(member);
        when(memberRedisRepository.findById(any())).thenReturn(Mono.just(memberRedis));

        // When
        final Mono<MemberDto> sut = memberService.getMember(memberId);

        // Then
        StepVerifier.create(sut)
                .assertNext(memberDto -> {
                    assertThat(memberDto.id()).isEqualTo(member.getId());
                    assertThat(memberDto.email()).isEqualTo(member.getEmail());
                    assertThat(memberDto.nickname()).isEqualTo(member.getNickname());
                })
                .verifyComplete();

        verify(memberRepository, times(0)).findById(anyLong());
    }

    @Test
    void testGetMemberDoOnError() {
        // Given
        final Long memberId = 1L;
        final Member member = Member.of("h@.id.e", "hide", "a", "a");
        when(memberRedisRepository.findById(any())).thenReturn(Mono.empty());
        when(memberRepository.findById(anyLong())).thenReturn(Mono.just(member));
        when(memberRedisRepository.save(any())).thenReturn(Mono.just(MemberRedis.from(member)));

        // When
        final Mono<MemberDto> sut = memberService.getMember(memberId);

        // Then
        StepVerifier.create(sut)
                .assertNext(memberDto -> {
                    assertThat(memberDto.id()).isEqualTo(member.getId());
                    assertThat(memberDto.email()).isEqualTo(member.getEmail());
                    assertThat(memberDto.nickname()).isEqualTo(member.getNickname());
                })
                .verifyComplete();

        verify(memberRepository, times(1)).findById(anyLong());
        verify(memberRedisRepository, times(1)).save(any());
    }

    @Test
    void testGetMemberInfoMemberIsEmpty() {
        // Given
        final Long memberId = 1L;
        when(memberRepository.findById(anyLong())).thenReturn(Mono.empty());

        // When
        final Mono<MemberInfoDto> sut = memberService.getMemberInfo(memberId);

        // Then
        StepVerifier.create(sut)
                .verifyError(RuntimeException.class);
    }

    @Test
    void testGetMemberInfo() {
        // Given
        final Long memberId = 1L;
        final Member member = Member.of("h@.id.e", "hide", "a", "a");
        when(memberRepository.findById(anyLong())).thenReturn(Mono.just(member));

        final GetOrderResponse getOrderResponse = GetOrderResponse.builder().orderId("1").build();
        when(orderClient.getOrder()).thenReturn(Mono.just(getOrderResponse));
        final GetPointResponse getPointResponse = GetPointResponse.builder().memberId(1L).point(10000).build();
        when(pointClient.getPoint()).thenReturn(Mono.just(getPointResponse));

        // When
        final Mono<MemberInfoDto> sut = memberService.getMemberInfo(memberId);

        // Then
        StepVerifier.create(sut)
                .assertNext(memberInfoDto -> {
                    assertThat(memberInfoDto.getMemberId()).isNull();
                    assertThat(memberInfoDto.getOrderId()).isEqualTo(getOrderResponse.getOrderId());
                    assertThat(memberInfoDto.getPoint()).isEqualTo(getPointResponse.getPoint());
                })
                .verifyComplete();
    }
}
