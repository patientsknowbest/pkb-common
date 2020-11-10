package com.pkb.common.config;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

@ExtendWith(MockitoExtension.class)
class SpringBootConfigStorageTest {

    private final Map<String, Object> lowPriorityProperties = new HashMap<>();

    private final StandardEnvironment environment = new StandardEnvironment();

    @Mock
    private RefreshScope refreshScope;

    private SpringBootConfigStorage underTest;

    @BeforeEach
    void setup() {
        underTest = new SpringBootConfigStorage(environment, refreshScope);
        environment.getPropertySources().addLast(new MapPropertySource("standardProperties", lowPriorityProperties));
    }

    @Test
    void cantOverrideMutableConfigProperty() {
        assertThat(underTest.isMutableConfigEnabled(), equalTo(false));
        underTest.setValue(ConfigStorage.MUTABLE_CONFIG_KEY, "true");
        assertThat(underTest.getBoolean(ConfigStorage.MUTABLE_CONFIG_KEY, false), equalTo(false));
        assertThat(underTest.isMutableConfigEnabled(), equalTo(false));
    }

    @Test
    void returnsOverrideForNormalProperty() {
        lowPriorityProperties.put(ConfigStorage.MUTABLE_CONFIG_KEY, "true");
        lowPriorityProperties.put("myProp", "myVal");
        assertThat(underTest.getString("myProp"), equalTo("myVal"));

        underTest.setValue("myProp", "myOverrideValue");
        assertThat(underTest.getString("myProp"), equalTo("myOverrideValue"));
    }

    @Test
    void fallsBackToDefaultForNonExistentProperty() {
        assertThat(underTest.getString("myProp", "myVal"), equalTo("myVal"));
    }

    @Test
    void resetsOverrideForPropertyCorrectly() {
        lowPriorityProperties.put(ConfigStorage.MUTABLE_CONFIG_KEY, "true");
        lowPriorityProperties.put("myProp", "myVal");
        underTest.setValue("myProp", "myOverrideValue");
        underTest.reset();
        assertThat(underTest.getString("myProp"), equalTo("myVal"));
    }

    @Test
    void removesOverrideForPropertyCorrectly() {
        lowPriorityProperties.put(ConfigStorage.MUTABLE_CONFIG_KEY, "true");
        lowPriorityProperties.put("myProp", "myVal");
        underTest.setValue("myProp", "myOverrideValue");
        assertThat(underTest.removeOverrideAtKey("myProp"), equalTo(OverrideRemovalResult.REMOVED));
        assertThat(underTest.getString("myProp"), equalTo("myVal"));
    }

    @Test
    void failsToRemoveNonExistentOverride() {
        lowPriorityProperties.put(ConfigStorage.MUTABLE_CONFIG_KEY, "true");
        lowPriorityProperties.put("myProp", "myVal");
        underTest.setValue("myProp", "myOverrideValue");
        assertThat(underTest.removeOverrideAtKey("myProp2"), equalTo(OverrideRemovalResult.KEY_NOT_FOUND));
        assertThat(underTest.getString("myProp"), equalTo("myOverrideValue"));
    }

    @Test
    void skipsOverrideRemovalForNonMutableConfig() {
        lowPriorityProperties.put("myProp", "myVal");
        underTest.setValue("myProp", "myOverrideValue");
        assertThat(underTest.removeOverrideAtKey("myProp2"), equalTo(OverrideRemovalResult.NO_OP_AS_CONFIG_IS_IMMUTABLE));
        assertThat(underTest.getString("myProp"), equalTo("myVal"));
    }

}
