package com.pkb.common.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PropertyFileBasedLoaderTest {

    @Test
    public void testCanSeeDefaults() {
        PropertyFileBasedLoader loader = new PropertyFileBasedLoader("config/default.properties");
        RawConfigStorage storage = loader.load();
        assertEquals("Patients Know Best", storage.getString("fromEmailName"));
    }
}
