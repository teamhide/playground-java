package com.teamhide.playground.gatekeeper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

public interface ReactiveLockManager {
    <T> Mono<T> executeWithLock(String identifier, Supplier<Mono<T>> supplier);
    <T> Flux<T> executeManyWithLock(String identifier, Supplier<Flux<T>> supplier);
}
