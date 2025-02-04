package com.teamhide.playground.beanprocessor.factorypostprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactoryPostProcessorConfig {
    @Bean
    public HideClientFactoryPostProcessor hideClientFactoryPostProcessor() {
        return new HideClientFactoryPostProcessor();
    }
}
