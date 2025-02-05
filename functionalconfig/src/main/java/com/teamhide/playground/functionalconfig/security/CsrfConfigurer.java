package com.teamhide.playground.functionalconfig.security;

public class CsrfConfigurer {
    private final ApplicationContext applicationContext;

    public CsrfConfigurer(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void disable() {
        System.out.println("CSRF disabled");
    }
}
