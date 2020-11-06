package com.pkb.common.config;

import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * This post-processor works around a bug/limitation of Spring Boot/Spring Cloud whereby
 * beans discovered by the @EnableConfigurationProperties or @ConfigurationPropertiesScan annotations
 * aren't ever put into custom scopes like the RefreshScope. This post-processor mimics what the
 * standard Spring bean component scanning does by reading the scope attributes and then creating
 * a scoped proxy if the requested scope requires it. It also ensure that the bean definition
 * has the appropriate scope marked on it.
 *
 * To use this, define a static method on Configuration class that returns an instance of this as a Bean.
 * You should probably give it an @Order attribute as well to ensure that this executes as early as possible
 * so that other standard scope processing steps can operate on the corrected bean definition.
 */
@ParametersAreNonnullByDefault
public class ConfigurationPropertiesScopingPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String name : registry.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(name);
            if (beanDefinition.getClass().getName().equals("org.springframework.boot.context.properties.ConfigurationPropertiesValueObjectBeanDefinition")) {
                GenericBeanDefinition gbd = (GenericBeanDefinition) beanDefinition;
                Optional<AnnotationAttributes> annAttrs = Optional.ofNullable(AnnotationAttributes.fromMap(AnnotationMetadata.introspect(gbd.getBeanClass()).getAnnotationAttributes(Scope.class.getName(), false)));
                ScopedProxyMode scopedProxyMode = annAttrs.map(attrs -> attrs.<ScopedProxyMode>getEnum("proxyMode")).orElse(ScopedProxyMode.NO);
                gbd.setScope(annAttrs.map(attrs -> attrs.getString("value")).orElse(gbd.getScope()));
                if (scopedProxyMode == ScopedProxyMode.NO) {
                    continue;
                }
                boolean proxyTargetClass = scopedProxyMode == ScopedProxyMode.TARGET_CLASS;
                registry.removeBeanDefinition(name);
                BeanDefinitionHolder scopedProxy = ScopedProxyUtils.createScopedProxy(new BeanDefinitionHolder(gbd, name), registry, proxyTargetClass);
                registry.registerBeanDefinition(scopedProxy.getBeanName(), scopedProxy.getBeanDefinition());
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //No-op
    }
}
