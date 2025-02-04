package com.teamhide.playground.beanprocessor.postprocessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeanPostProcessorConfigTest {
    @Test
    void testBeanPostProcessor() {
        // Given
        final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // When, Then
        applicationContext.getBean("classicCar", RacingCar.class);
        applicationContext.getBean("racingCar", ClassicCar.class);
        assertThrows(BeanNotOfRequiredTypeException.class, () -> applicationContext.getBean("racingCar", RacingCar.class));
        assertThrows(BeanNotOfRequiredTypeException.class, () -> applicationContext.getBean("classicCar", ClassicCar.class));
    }
}
