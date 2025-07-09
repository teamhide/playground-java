package com.teamhide.playground.gatekeeper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SpringBootTest(classes = LockApplication.class)
@Import({
        RedisTestConfiguration.class,
        MySqlTestConfiguration.class,
        MySqlReactiveTestConfiguration.class
})
public @interface IntegrationTest {}
