package com.teamhide.playground.gatekeeper.mysql.reactive;

import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
public class ReactiveMySqlLockAutoConfiguration {
    @Configuration
    @ConditionalOnBean(ConnectionFactory.class)
    @ConditionalOnClass(ConnectionFactory.class)
    public static class MySqlLockProviderAutoConfiguration {
        @Bean
        public ReactiveMySqlLockProvider reactiveMySqlLockProvider(final ConnectionFactory connectionFactory) {
            log.info("Creating Reactive MySql Lock Provider");
            return new ReactiveMySqlLockProvider(connectionFactory);
        }
    }
}
