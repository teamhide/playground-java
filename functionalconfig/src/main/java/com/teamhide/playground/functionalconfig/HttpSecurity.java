package com.teamhide.playground.functionalconfig;

public class HttpSecurity {
    private ApplicationContext applicationContext;

    public HttpSecurity(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public HttpSecurity configureRequests(final Customizer<RequestConfigurer> customizer) {
        final RequestConfigurer requestConfigurer = new RequestConfigurer(applicationContext);
        customizer.customize(requestConfigurer);
        return this;
    }

    public HttpSecurity configureSession(final Customizer<SessionConfigurer> customizer) {
        final SessionConfigurer sessionConfigurer = new SessionConfigurer(applicationContext);
        customizer.customize(sessionConfigurer);
        return this;
    }

    public HttpSecurity csrf(final Customizer<CsrfConfigurer> customizer) {
        final CsrfConfigurer csrfConfigurer = new CsrfConfigurer(applicationContext);
        customizer.customize(csrfConfigurer);
        return this;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
