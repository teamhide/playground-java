package teamhide.playground.httpinterface.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Objects;
import java.util.Set;

@Slf4j
@Component
public class HttpInterfaceBeanRegistrar implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
        final HttpInterfaceBeanFactory httpInterfaceBeanFactory = new HttpInterfaceBeanFactory();
        final Environment environment = beanFactory.getBean(Environment.class);

        final Set<BeanDefinition> beanDefinitions = httpInterfaceBeanFactory.findBeanDefinitions(environment);
        beanDefinitions.stream()
                .filter(definition -> StringUtils.hasText(definition.getBeanClassName()))
                .forEach(definition -> registerHttpClient(beanFactory, definition, environment));
    }

    private <C> C createHttpClient(final Class<C> interfaceClass, final Environment environment) {
        final HttpExchange httpExchangeAnnotation = AnnotationUtils.getAnnotation(interfaceClass, HttpExchange.class);
        if (httpExchangeAnnotation == null) {
            throw new IllegalStateException("The interface " + interfaceClass.getName() + " is missing @HttpExchange annotation");
        }

        final String url = resolveUrl(httpExchangeAnnotation.url(), environment);
        if (!StringUtils.hasText(url)) {
            throw new IllegalArgumentException("The URL for " + interfaceClass.getName() + " must not be empty");
        }

        final RestClient restClient = RestClient.builder().baseUrl(url).build();
        final RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory proxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return proxyFactory.createClient(interfaceClass);
    }

    private void registerHttpClient(
            final ConfigurableListableBeanFactory beanFactory,
            final BeanDefinition beanDefinition,
            final Environment environment) {

        final Class<?> interfaceClass = findInterface(beanDefinition);
        final Object client = createHttpClient(interfaceClass, environment);

        log.info("Registering HTTP client for bean {}", beanDefinition.getBeanClassName());
        beanFactory.registerSingleton(Objects.requireNonNull(beanDefinition.getBeanClassName()), client);
    }

    private Class<?> findInterface(final BeanDefinition beanDefinition) {
        try {
            return ClassUtils.forName(
                    Objects.requireNonNull(beanDefinition.getBeanClassName()),
                    this.getClass().getClassLoader());
        } catch (final ClassNotFoundException e) {
            throw new IllegalStateException("Failed to load class: " + beanDefinition.getBeanClassName(), e);
        }
    }

    private String resolveUrl(final String urlExpression, final Environment environment) {
        if (urlExpression.startsWith("${") && urlExpression.endsWith("}")) {
            final String propertyKey = urlExpression.substring(2, urlExpression.length() - 1);
            final String resolvedUrl = environment.getProperty(propertyKey);
            if (!StringUtils.hasText(resolvedUrl)) {
                throw new IllegalArgumentException("Property '" + propertyKey + "' is not defined in the environment");
            }
            return resolvedUrl;
        }
        return urlExpression;
    }
}
