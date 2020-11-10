package com.pkb.common.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

import com.pkb.common.config.SpringBootConfigStorageIntegrationTest.TestConfiguration.TestConfigProperties;

@SpringBootTest
public class SpringBootConfigStorageIntegrationTest {

    @Configuration
    @EnableConfigurationProperties(TestConfigProperties.class)
    @EnableAutoConfiguration
    public static class TestConfiguration {
        @Bean
        @Order
        public static ConfigurationPropertiesScopingPostProcessor configurationPropertiesScopingPostProcessor() {
            return new ConfigurationPropertiesScopingPostProcessor();
        }

        @Bean
        public SpringBootConfigStorage configStorage(ConfigurableEnvironment environment, RefreshScope refreshScope) {
            return new SpringBootConfigStorage(environment, refreshScope);
        }

        @ConfigurationProperties(prefix = "com.pkb.common.config")
        @ConstructorBinding
        @org.springframework.cloud.context.config.annotation.RefreshScope
        public static class TestConfigProperties {
            private final int testVal;

            public TestConfigProperties(int testVal) {
                this.testVal = testVal;
            }

            public int getTestVal() {
                return testVal;
            }
        }
    }

    @Autowired
    private SpringBootConfigStorage configStorage;

    @Autowired
    TestConfigProperties props;

    @Test
    void returnsValueFromAppProperties() {
        assertThat(props.getTestVal(), equalTo(10));
    }

    @Test
    void overridesCorrectly() {
        configStorage.setValue("com.pkb.common.config.test-val", "50");
        assertThat(props.getTestVal(), equalTo(50));
    }

    @Test
    void resetsCorrectly() {
        configStorage.setValue("com.pkb.common.config.test-val", "50");
        configStorage.reset();
        assertThat(props.getTestVal(), equalTo(10));

    }

}
