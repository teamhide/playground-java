package com.teamhide.playground.gatekeeper;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(classes = LockApplication.class)
@Import(RedisTestConfiguration.class)
@EnableAutoConfiguration(
        exclude = {DataSourceAutoConfiguration.class, R2dbcAutoConfiguration.class})
@Tag("integration")
public @interface RedisIntegrationTest {}
