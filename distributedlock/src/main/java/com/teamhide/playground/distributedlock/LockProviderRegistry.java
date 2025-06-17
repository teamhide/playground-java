package com.teamhide.playground.distributedlock;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class LockProviderRegistry {
    private final Map<String, LockProvider> typeToProvider ;

    public LockProviderRegistry(final List<LockProvider> providers) {
        this.typeToProvider = providers.stream()
                .peek(provider ->
                        log.info("Registering LockProvider: type='{}', instance={}", provider.type(), provider.getClass().getSimpleName())
                )
                .collect(Collectors.toMap(
                        LockProvider::type,
                        Function.identity(),
                        (existing, replacement) -> {
                            throw new IllegalStateException("Duplicate LockProvider: " + existing.type());
                        }
                    )
                );
    }

    public LockProvider get(final String type) {
        final LockProvider lockProvider = typeToProvider.get(type);
        if (lockProvider == null) {
            throw new IllegalStateException("No LockProvider found for provider: " + type);
        }
        return lockProvider;
    }
}
