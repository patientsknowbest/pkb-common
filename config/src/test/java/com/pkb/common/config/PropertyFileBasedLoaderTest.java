package com.pkb.common.config;

import org.junit.jupiter.api.Test;

import static com.github.karsaig.approvalcrest.jupiter.MatcherAssert.assertThrows;
import static com.github.karsaig.approvalcrest.jupiter.matcher.Matchers.sameBeanAs;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class PropertyFileBasedLoaderTest {


    @Test
    public void load_validPath_succesfullyLoads() {
        PropertyFileBasedLoader loader = new PropertyFileBasedLoader("config/default.properties");
        ImmutableRawConfigStorage storage = loader.load();
        assertEquals("Patients Know Best Help Team", storage.getString("helpName"));
    }

    @Test
    public void load_fileNotPresent_throwsException() {
        var expected = new ConfigurationException("could not find property file [config/non-existant.properties]");

        assertThrows(sameBeanAs(expected), () -> {
            PropertyFileBasedLoader loader = new PropertyFileBasedLoader("config/non-existant.properties");
            ImmutableRawConfigStorage storage = loader.load();
        });
    }
}
