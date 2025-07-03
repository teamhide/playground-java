package com.teamhide.playground.gatekeeper.mysql.reactive;

import org.slf4j.MDC;
import org.springframework.util.CollectionUtils;
import reactor.util.context.Context;

import java.util.Map;

public class MdcUtil {
    private MdcUtil() {}

    public static Context mdcToReactorContext() {
        final Map<String, String> contextMap = MDC.getCopyOfContextMap();
        final Context ctx = Context.empty();
        if (CollectionUtils.isEmpty(contextMap)) {
            return ctx;
        }

        for (Map.Entry<String, String> entry : contextMap.entrySet()) {
            ctx.put(entry.getKey(), entry.getValue());
        }
        return ctx;
    }
}
