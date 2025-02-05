package com.teamhide.playground.functionalconfig.security;

public class SessionConfigurer {
    private final ApplicationContext applicationContext;

    public SessionConfigurer(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void sessionCreationPolicy(final SessionCreationPolicy policy) {
        System.out.println("Session policy: " + policy);
    }
}
