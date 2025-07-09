package com.teamhide.playground.gatekeeper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class LockAutoConfigurationNoLockProviderTest {
    @Test
    @DisplayName("provider에 해당하는 LockProvider 빈이 없으면 예외를 던지며 서버 시작에 실패한다")
    void testMissingProviderShouldFailContext() {
        ApplicationContextRunner contextRunner =
                new ApplicationContextRunner()
                        .withConfiguration(AutoConfigurations.of(LockAutoConfiguration.class))
                        .withUserConfiguration(TestConfiguration.class)
                        .withPropertyValues(
                                "lock.instances.count.provider=redis",
                                "lock.instances.count.key=count",
                                "lock.instances.count.wait-time-milli-seconds=0",
                                "lock.instances.count.lease-time-milli-seconds=1000");

        contextRunner.run(
                context -> {
                    Throwable failure = context.getStartupFailure();
                    assertThat(failure)
                            .isInstanceOf(BeanCreationException.class)
                            .hasRootCauseInstanceOf(DistributedLockException.class)
                            .hasRootCauseMessage(
                                    "No LockProvider found for type 'redis' required by lock 'count'");
                });
    }

    @Test
    @DisplayName("Reactive provider에 해당하는 ReactiveLockProvider 빈이 없으면 예외를 던지며 서버 시작에 실패한다")
    void testMissingReactiveProviderShouldFailContext() {
        final ApplicationContextRunner contextRunner =
                new ApplicationContextRunner()
                        .withConfiguration(AutoConfigurations.of(LockAutoConfiguration.class))
                        .withUserConfiguration(TestConfiguration.class)
                        .withPropertyValues(
                                "lock.instances.count.provider=redis-reactive",
                                "lock.instances.count.key=count",
                                "lock.instances.count.wait-time-milli-seconds=0",
                                "lock.instances.count.lease-time-milli-seconds=1000");

        contextRunner.run(
                context -> {
                    final Throwable failure = context.getStartupFailure();
                    assertThat(failure)
                            .isInstanceOf(BeanCreationException.class)
                            .hasRootCauseInstanceOf(DistributedLockException.class)
                            .hasMessageContaining(
                                    "No LockProvider found for type 'redis-reactive' required by lock 'count'");
                });
    }

    @Configuration
    @EnableAutoConfiguration
    static class TestConfiguration {}
}
