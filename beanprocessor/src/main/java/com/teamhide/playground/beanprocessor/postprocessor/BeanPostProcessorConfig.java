package com.teamhide.playground.beanprocessor.postprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanPostProcessorConfig {
    @Bean
    public RacingCar racingCar() {
        return new RacingCar();
    }

    @Bean
    public ClassicCar classicCar() {
        return new ClassicCar();
    }

    @Bean
    public CarBeanPostProcessor carBeanPostProcessor() {
        return new CarBeanPostProcessor();
    }
}
