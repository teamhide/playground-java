package com.teamhide.playground.functionalconfig.security;

@FunctionalInterface
public interface Customizer<T> {
    void customize(T t);
}
