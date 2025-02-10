package com.teamhide.playground.webfluxworld.repository.rdb;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {
}
