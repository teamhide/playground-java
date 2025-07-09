package com.teamhide.playground.gatekeeper;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@TestConfiguration
@Testcontainers
public class RedisReactiveTestConfiguration {
    private static final String REDIS_IMAGE = "redis:latest";
    private static final int REDIS_PORT = 6379;

    @Container
    private static final GenericContainer<?> redisContainer =
            new GenericContainer<>(REDIS_IMAGE)
                    .withExposedPorts(REDIS_PORT)
                    .waitingFor(Wait.forListeningPort())
                    .withStartupTimeout(Duration.ofSeconds(10));

    static {
        redisContainer.start();
    }

    @Bean
    public RedissonClient redissonClient() {
        final Config config = new Config();
        final String host = redisContainer.getHost();
        final Integer port = redisContainer.getMappedPort(REDIS_PORT);
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        final String host = redisContainer.getHost();
        final Integer port = redisContainer.getMappedPort(REDIS_PORT);
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedissonReactiveClient redissonReactiveClient(final RedissonClient redissonClient) {
        return redissonClient.reactive();
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(
            final RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        final StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        return redisTemplate;
    }
}
