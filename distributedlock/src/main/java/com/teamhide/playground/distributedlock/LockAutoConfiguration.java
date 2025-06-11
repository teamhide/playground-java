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
    public LockProviderRegistry lockProviderRegistry(final List<LockProvider> lockProviders) {
        return new LockProviderRegistry(lockProviders);
    }

    @Bean
    public LockRegistry lockRegistry(final LockProviderRegistry lockProviderRegistry) {
        return new LockRegistry(lockProviderRegistry);
    }
}
