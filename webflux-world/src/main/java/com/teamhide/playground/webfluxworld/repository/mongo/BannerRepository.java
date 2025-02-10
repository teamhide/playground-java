package com.teamhide.playground.webfluxworld.repository.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BannerRepository extends ReactiveMongoRepository<Banner, String> {
}
