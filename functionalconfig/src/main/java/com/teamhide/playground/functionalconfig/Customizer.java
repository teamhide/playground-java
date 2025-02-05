package com.teamhide.playground.functionalconfig;

@FunctionalInterface
public interface Customizer<T> {
    void customize(T t);
}
