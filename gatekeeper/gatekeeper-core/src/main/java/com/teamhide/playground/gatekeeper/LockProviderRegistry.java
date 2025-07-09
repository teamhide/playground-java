package com.teamhide.playground.gatekeeper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LockProviderRegistry {
    private final Map<String, LockProvider> typeToProvider;

    public LockProviderRegistry(final List<LockProvider> providers) {
        this.typeToProvider =
                providers.stream()
                        .peek(
                                provider ->
                                        log.info(
                                                "Registering LockProvider: type='{}', instance={}",
                                                provider.type(),
                                                provider.getClass().getSimpleName()))
                        .collect(
                                Collectors.toMap(
                                        LockProvider::type,
                                        Function.identity(),
                                        (existing, replacement) -> {
                                            throw new DistributedLockException(
                                                    "Duplicate LockProvider: " + existing.type());
                                        }));
    }

    public LockProvider get(final String type) {
        final LockProvider provider = typeToProvider.get(type);
        if (provider == null) {
            throw new DistributedLockException("No LockProvider found for type: " + type);
        }
        return provider;
    }

    public boolean contains(final String type) {
        return typeToProvider.containsKey(type);
    }
}
