package teamhide.playground.httpinterface.clients;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.env.Environment;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.service.annotation.HttpExchange;
import teamhide.playground.httpinterface.Application;

import java.util.Set;

public class HttpInterfaceBeanFactory {
    public Set<BeanDefinition> findBeanDefinitions(final Environment environment) {
        final ClassPathScanningCandidateComponentProvider interfaceScanner =
                new ClassPathScanningCandidateComponentProvider(false, environment) {
                    @Override
                    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                        return beanDefinition.getMetadata().isInterface() &&
                                beanDefinition.getMetadata().hasAnnotation(HttpExchange.class.getName());
                    }
                };

        interfaceScanner.addIncludeFilter(new AnnotationTypeFilter(HttpExchange.class));
        final String basePackage = Application.class.getPackage().getName();
        return interfaceScanner.findCandidateComponents(basePackage);
    }
}
