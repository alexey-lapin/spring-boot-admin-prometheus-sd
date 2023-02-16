package com.github.alexeylapin.sbapsd.config;

import com.github.alexeylapin.sbapsd.service.ServiceProvider;
import com.github.alexeylapin.sbapsd.service.ServiceProviderRegistry;
import com.github.alexeylapin.sbapsd.service.factory.CompositeInstanceProviderFactory;
import com.github.alexeylapin.sbapsd.service.factory.DefaultFilterFactory;
import com.github.alexeylapin.sbapsd.service.factory.DefaultServiceProviderFactory;
import com.github.alexeylapin.sbapsd.service.factory.FilterFactory;
import com.github.alexeylapin.sbapsd.service.factory.InstanceProviderFactory;
import com.github.alexeylapin.sbapsd.service.factory.ServiceProviderFactory;
import com.github.alexeylapin.sbapsd.service.factory.V2InstanceRegistryInstanceProviderFactory;
import com.github.alexeylapin.sbapsd.service.factory.V2WebInstanceProviderFactory;
import com.github.alexeylapin.sbapsd.web.ReactiveHandlerMapping;
import com.github.alexeylapin.sbapsd.web.ServiceDiscoveryController;
import com.github.alexeylapin.sbapsd.web.ServletHandlerMapping;
import de.codecentric.boot.admin.server.services.InstanceRegistry;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@ConditionalOnBean(ServiceDiscoveryMarkerConfiguration.Marker.class)
@AutoConfigureAfter(name = "de.codecentric.boot.admin.server.config.AdminServerAutoConfiguration")
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ServiceDiscoveryProperties.class)
public class ServiceDiscoveryAutoConfiguration {

    @ConditionalOnClass(InstanceRegistry.class)
    @Configuration(proxyBeanMethods = false)
    public static class V2InstanceRegistryConfiguration {

        @ConditionalOnBean(InstanceRegistry.class)
        @ConditionalOnMissingBean
        @Bean
        public V2InstanceRegistryInstanceProviderFactory v2InstanceRegistryInstanceProviderFactory(
                InstanceRegistry instanceRegistry) {
            return new V2InstanceRegistryInstanceProviderFactory(instanceRegistry);
        }

    }

    @ConditionalOnMissingBean
    @Bean
    public V2WebInstanceProviderFactory v2WebInstanceProviderFactory() {
        return new V2WebInstanceProviderFactory();
    }

    @ConditionalOnMissingBean
    @Bean
    public CompositeInstanceProviderFactory compositeInstanceProviderFactory(List<InstanceProviderFactory> factories) {
        return new CompositeInstanceProviderFactory(factories);
    }

    @ConditionalOnMissingBean
    @Bean
    public DefaultFilterFactory filterFactory() {
        return new DefaultFilterFactory();
    }

    @ConditionalOnMissingBean
    @Bean
    public ServiceProviderFactory serviceProviderFactory(InstanceProviderFactory compositeInstanceProviderFactory,
                                                         FilterFactory filterFactory) {
        return new DefaultServiceProviderFactory(compositeInstanceProviderFactory, filterFactory);
    }

    @ConditionalOnMissingBean
    @Bean
    public ServiceProviderRegistry serviceProviderRegistry(ServiceDiscoveryProperties properties,
                                                           ServiceProviderFactory serviceProviderFactory) {
        Map<String, ServiceProvider> map = serviceProviderFactory.createAll(properties.getServers());
        return new ServiceProviderRegistry(Collections.unmodifiableMap(map));
    }

    @ConditionalOnMissingBean
    @Bean
    public ServiceDiscoveryController serviceDiscoveryController(ServiceProviderRegistry serviceProviderRegistry) {
        return new ServiceDiscoveryController(serviceProviderRegistry);
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Configuration(proxyBeanMethods = false)
    public static class ReactiveRestApiConfiguration {

        @Bean
        public ReactiveHandlerMapping reactiveHandlerMapping(RequestedContentTypeResolver webFluxContentTypeResolver) {
            ReactiveHandlerMapping mapping = new ReactiveHandlerMapping();
            mapping.setOrder(0);
            mapping.setContentTypeResolver(webFluxContentTypeResolver);
            return mapping;
        }

    }

    @AutoConfigureAfter(WebMvcAutoConfiguration.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Configuration(proxyBeanMethods = false)
    public static class ServletRestApiConfiguration {

        @Bean
        public ServletHandlerMapping servletHandlerMapping(ContentNegotiationManager contentNegotiationManager) {
            ServletHandlerMapping mapping = new ServletHandlerMapping();
            mapping.setOrder(0);
            mapping.setContentNegotiationManager(contentNegotiationManager);
            return mapping;
        }

    }

}
