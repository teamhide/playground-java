package com.teamhide.playground.distributedlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LockProviderRegistry {
    private final Map<String, LockProvider> typeToProvider = new HashMap<>();

    public LockProviderRegistry(final List<LockProvider> providers) {
        this.typeToProvider.putAll(providers.stream().collect(Collectors.toMap(LockProvider::type, Function.identity())));
    }

    public LockProvider getProvider(final String type) {
        return typeToProvider.get(type);
    }

    public boolean hasProvider(final String type) {
        return typeToProvider.containsKey(type);
    }

    public Map<String, LockProvider> getAllProviders() {
        return Map.copyOf(typeToProvider);
    }
}
