package com.teamhide.playground.gatekeeper.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedisLockAutoConfiguration {
    @Configuration
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnClass(RedissonClient.class)
    public static class RedisLockProviderAutoConfiguration {
        @Bean
        public RedissonLockProvider redissonLockProvider(final RedissonClient redissonClient) {
            log.info("Creating Redisson Lock Provider");
            return new RedissonLockProvider(redissonClient);
        }
    }
}
