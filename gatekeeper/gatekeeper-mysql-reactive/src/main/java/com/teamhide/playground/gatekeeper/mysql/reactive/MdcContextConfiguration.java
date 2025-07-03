package com.teamhide.playground.gatekeeper.mysql.reactive;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

public class MdcContextConfiguration implements InitializingBean, DisposableBean {
    private static final String HOOK_KEY = "mdcContext";

    @Override
    public void destroy() throws Exception {
        Hooks.onEachOperator(
                HOOK_KEY,
                Operators.lift(((scannable, coreSubscriber) -> new MdcContextLifter<>(coreSubscriber)))
        );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Hooks.resetOnEachOperator(HOOK_KEY);
    }
}
