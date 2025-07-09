package com.teamhide.playground.gatekeeper;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ReactiveLockProviderRegistry {
    private final Map<String, ReactiveLockProvider> typeToProvider;

    public ReactiveLockProviderRegistry(final List<ReactiveLockProvider> providers) {
        this.typeToProvider =
                providers.stream()
                        .peek(
                                provider ->
                                        log.info(
                                                "Registering ReactiveLockProvider: type='{}', instance={}",
                                                provider.type(),
                                                provider.getClass().getSimpleName()))
                        .collect(
                                Collectors.toMap(
                                        ReactiveLockProvider::type,
                                        Function.identity(),
                                        (existing, replacement) -> {
                                            throw new DistributedLockException(
                                                    "Duplicate ReactiveLockProvider: " + existing.type());
                                        }));
    }

    public ReactiveLockProvider get(final String type) {
        final ReactiveLockProvider provider = typeToProvider.get(type);
        if (provider == null) {
            throw new DistributedLockException("No ReactiveLockProvider found for type: " + type);
        }
        return provider;
    }

    public boolean contains(final String type) {
        return typeToProvider.containsKey(type);
    }
}
