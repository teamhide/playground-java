package com.teamhide.playground.distributedlock;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(LockRegistry.class)
@Slf4j
public class LockAutoConfiguration {
    @Configuration
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnClass(RedissonClient.class)
    public static class RedisLockProviderAutoConfiguration {
        @Bean
        public RedisLockProvider redisLockProvider(final RedissonClient redissonClient) {
            log.info("Creating Redis Lock Provider");
            return new RedisLockProvider(redissonClient);
        }
    }

    @Configuration
    @ConditionalOnBean(DataSource.class)
    @ConditionalOnClass(DataSource.class)
    public static class MySqlLockProviderAutoConfiguration {
        @Bean
        public MySqlLockProvider mySqlLockProvider(final DataSource dataSource) {
            log.info("Creating MySql Lock Provider");
            return new MySqlLockProvider(dataSource);
        }
    }

    @Bean
    public LockRegistry lockRegistry(final List<LockProvider> lockProviders) {
        final Map<String, LockProvider> providers = lockProviders.stream()
                .collect(Collectors.toMap(LockProvider::type, Function.identity()));
        providers.forEach((k, v) -> log.info("Registering lock provider '{}', value={}", k, v));
        return new LockRegistry(providers);
    }
}
