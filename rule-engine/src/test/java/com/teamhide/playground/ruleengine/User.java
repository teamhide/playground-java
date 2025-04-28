package com.teamhide.playground.ruleengine;

public class User {
    private final int age;
    private final String name;

    public User(final int age, final String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}
