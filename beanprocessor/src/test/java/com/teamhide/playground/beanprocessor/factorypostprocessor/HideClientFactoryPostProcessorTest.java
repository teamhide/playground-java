package com.teamhide.playground.beanprocessor.factorypostprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class HideClientFactoryPostProcessorTest {
    @Autowired private ApplicationContext applicationContext;

    @Test
    void test() {
        // Given, When
        final Map<String, HideClientImpl> beansOfType = applicationContext.getBeansOfType(HideClientImpl.class);

        // Then
        assertThat(beansOfType.size()).isEqualTo(2);
    }
}
