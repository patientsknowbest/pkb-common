package com.pkb.common.config;

import static org.junit.Assert.assertEquals;

import javax.security.auth.login.LoginException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PropertyFileBasedLoaderTest {

    private ExpectedException exceptionRule = ExpectedException.none();

    @Rule
    public ExpectedException getRule() {
        return exceptionRule;
    }

    @Test
    public void load_validPath_succesfullyLoads() {
        PropertyFileBasedLoader loader = new PropertyFileBasedLoader("config/default.properties");
        RawConfigStorage storage = loader.load();
        assertEquals("Patients Know Best Help Team", storage.getString("helpName"));
    }

    @Test
    public void load_fileNotPresent_throwsException() {
        exceptionRule.expect(ConfigurationException.class);
        exceptionRule.expectMessage("could not find property file [config/non-existant.properties]");

        PropertyFileBasedLoader loader = new PropertyFileBasedLoader("config/non-existant.properties");
        RawConfigStorage storage = loader.load();
    }
}
