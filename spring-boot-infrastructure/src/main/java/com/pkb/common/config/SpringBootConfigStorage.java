package com.pkb.common.config;

import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class provides an implementation of the PKB ConfigStorage interface that interacts
 * helpfully with the Spring environment. This class can be passed to both the TestSupportAgent
 * and any PKB config objects that require a ConfigStorage. It will read properties from the
 * Spring environment, allowing Spring Boot applications to be be configured using all the standard
 * Spring properties mechanisms such as application.properties and the OS environment - so you don't
 * need to provide an additional config.properties file just to control shared PKB components that
 * use PKB Common configuration classes
 *
 * Additionally, this class maintains an override map that is loaded into the Spring environment
 * as a high-priority property source. If mutations to properties are requested (e.g. from the
 * TestSupportAgent) these overrides will be visible in the spring environment.
 *
 * In a typical Spring Boot application, properties are not accessed directly from the environment but
 * are bound to type-safe classes annotated with @ConfigurationProperties. Since this binding happens when
 * the Spring context is loading, in order for later changes to the Spring environment to be visible
 * to these classes and their clients, it is necessary to do a selective refresh of the spring context.
 * ConfigurationProperties beans that contain mutable properties that may need to be updated at runtime
 * should be annotated with @RefreshScope. Additionally, you should include the {@link ConfigurationPropertiesScopingPostProcessor}
 * bean in your context to ensure that all ConfigurationProperties classes get included in the RefreshScope,
 * irrespective of how they are constructed. Once you do this, this class will ensure that configuration
 * beans are dynamically re-bound at runtime when properties change.
 *
 * It is recommended to use @ConstructorBinding for ConfigurationProperties classes; this can be used with
 * kotlin data classes, java value objects or at least a POJO with final fields and no setters. This ensures
 * that properties present an immutable interface to clients and can only be changed by the framework.
 *
 *
 * NB This class and the above-described mechanism only ensure that the relevant properties classes will
 * return the updated values on the next method call to one of the property getters. As yet there is no
 * mechanism for notifying clients that an update has occured (although in principle this would not be difficult)
 * nor does it make any attempt to ensure that any dependencies of these properties beans - including spring
 * infrastructure that may have been wired together according to their values - are dynamically updated.
 */
@ParametersAreNonnullByDefault
public class SpringBootConfigStorage extends AbstractBaseConfigStorage implements ImmutableConfigStorage {
    private final ConfigurableEnvironment environment;
    private final RefreshScope refreshScope;
    private final Map<String, Object> overrides = new LinkedHashMap<>();

    public SpringBootConfigStorage(ConfigurableEnvironment environment, RefreshScope refreshScope) {
        this.environment = environment;
        this.refreshScope = refreshScope;
        environment.getPropertySources().addFirst(new MapPropertySource("TestSupportOverrides", overrides));
    }


    @Override
    public String getString(String key) {
        return environment.getProperty(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    @Override
    public boolean isMutableConfigEnabled() {
        return getBoolean("mutableConfig.enabled", false);
    }

    @Override
    public void setValue(String key, String value) {
        if (isMutableConfigEnabled()) {
            overrides.put(key, value);
            refreshScope.refreshAll();
        }
    }

    @Override
    public OverrideRemovalResult removeOverrideAtKey(String key) {
        if (!isMutableConfigEnabled()) {
            return OverrideRemovalResult.NO_OP_AS_CONFIG_IS_IMMUTABLE;
        }
        if (overrides.remove(key) == null) {
            return OverrideRemovalResult.KEY_NOT_FOUND;
        }
        refreshScope.refreshAll();
        return OverrideRemovalResult.REMOVED;
    }

    @Override
    public void reset() {
        if (isMutableConfigEnabled()) {
            overrides.clear();
            refreshScope.refreshAll();
        }
    }

}

