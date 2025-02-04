package com.teamhide.playground.beanprocessor.factorypostprocessor;

import com.teamhide.playground.beanprocessor.Application;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Objects;
import java.util.Set;

@Slf4j
public class HideClientFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final String packageName = Application.class.getPackage().getName();
        final ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                return beanDefinition.getMetadata().isInterface() &&
                        beanDefinition.getMetadata().hasAnnotation(HideClient.class.getName());
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(HideClient.class));
        final Set<BeanDefinition> candidates = scanner.findCandidateComponents(packageName);
        candidates.forEach(candidate -> {
            log.info("Register client: {}", candidate.getBeanClassName());
            beanFactory.registerSingleton(Objects.requireNonNull(candidate.getBeanClassName()), new HideClientImpl());
        });
    }
}
