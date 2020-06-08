package com.pkb.common.config;


import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameJsonAsApproved;

import org.junit.jupiter.api.Test;

//a5b836
public class ConfigLoaderTest {

    //aaa4a6
    @Test
    public void testReadConfiguration() {
        ImmutableRawConfigStorage baseConfiguration = new PropertyFileBasedLoader("base_config.properties").load();
        assertThat(baseConfiguration, sameJsonAsApproved());
    }

    //fb1287
    @Test
    public void testMergeConfiguration() {
        ImmutableRawConfigStorage merged = LayeredLoader.createLoaderForEnvVar("base_config:layer1_config").load();
        assertThat(merged, sameJsonAsApproved());
    }
}
