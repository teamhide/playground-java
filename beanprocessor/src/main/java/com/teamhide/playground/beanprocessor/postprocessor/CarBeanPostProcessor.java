package com.teamhide.playground.beanprocessor.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Slf4j
public class CarBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RacingCar) {
            log.info("RacingCar to ClassicCar, bean={}, beanName={}", bean, beanName);
            return new ClassicCar();
        } else if (bean instanceof ClassicCar) {
            log.info("ClassicCar to RacingCar, bean={}, beanName={}", bean, beanName);
            return new RacingCar();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
