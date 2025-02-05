package com.teamhide.playground.functionalconfig;

public class RequestConfigurer {
    private final ApplicationContext applicationContext;

    public RequestConfigurer(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void allowAllRequests() {
        System.out.println("Allowing all requests");
    }

    public void denyAllRequests() {
        System.out.println("Denying all requests");
    }
}
