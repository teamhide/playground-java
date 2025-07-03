package com.teamhide.playground.gatekeeper.mysql.reactive;

import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.util.Map;
import java.util.stream.Collectors;

public class MdcContextLifter<T> implements CoreSubscriber<T> {

    private final CoreSubscriber<? super T> actual;

    public MdcContextLifter(final CoreSubscriber<? super T> actual) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe(final Subscription s) {
        actual.onSubscribe(s);
    }

    @Override
    public void onNext(final T t) {
        copyToMdc(actual.currentContext());
        actual.onNext(t);
    }

    @Override
    public void onError(final Throwable t) {
        actual.onError(t);
    }

    @Override
    public void onComplete() {
        actual.onComplete();
    }

    @Override
    public Context currentContext() {
        return actual.currentContext();
    }

    private void copyToMdc(final Context context) {
        if (context != null && !context.isEmpty()) {
            final Map<String, String> map = context.stream()
                            .collect(Collectors.toMap(e ->
                                    e.getKey().toString(),
                                    e -> e.getValue().toString()
                            ));
            MDC.setContextMap(map);
        } else {
            MDC.clear();
        }
    }
}
