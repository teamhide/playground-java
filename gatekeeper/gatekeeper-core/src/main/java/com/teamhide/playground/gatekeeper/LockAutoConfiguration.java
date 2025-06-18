package com.teamhide.playground.gatekeeper;

import com.teamhide.playground.gatekeeper.config.LockConfigLoader;
import com.teamhide.playground.gatekeeper.config.LockInstances;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(LockInstances.class)
@Slf4j
public class LockAutoConfiguration {
    @Bean
    public LockProviderRegistry lockProviderRegistry(final List<LockProvider> lockProviders) {
        return new LockProviderRegistry(lockProviders);
    }

    @Bean
    public LockConfigLoader lockConfigLoader(final LockInstances instances) {
        return new LockConfigLoader(instances);
    }

    @Bean
    public LockRegistry lockRegistry(final LockProviderRegistry lockProviderRegistry, final LockConfigLoader lockConfigLoader) {
        return new LockRegistry(lockProviderRegistry, lockConfigLoader);
    }
}
