package com.teamhide.playground.functionalconfig.car;

@FunctionalInterface
public interface CarCustomizer<T> {
    void customize(T t);
}
