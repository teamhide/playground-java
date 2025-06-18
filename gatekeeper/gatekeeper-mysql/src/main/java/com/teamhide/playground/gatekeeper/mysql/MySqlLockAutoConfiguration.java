package com.teamhide.playground.gatekeeper.mysql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class MySqlLockAutoConfiguration {
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
}
