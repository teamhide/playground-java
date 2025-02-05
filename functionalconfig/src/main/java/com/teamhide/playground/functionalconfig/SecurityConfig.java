package com.teamhide.playground.functionalconfig;

public class SecurityConfig {
    public static void main(String[] args) {
        final ApplicationContext applicationContext = new ApplicationContext();
        final HttpSecurity httpSecurity = new HttpSecurity(applicationContext);
        httpSecurity
                .csrf(CsrfConfigurer::disable)
                .configureRequests(request -> {
                    request.allowAllRequests();
                    request.denyAllRequests();
                })
                .configureSession(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    }
}
