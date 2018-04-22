package com.pkb.common.config;

import org.junit.Test;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ConfigLoaderTest {


    @Test
    public void testReadConfiguration() throws Exception {
        RawConfigStorage baseConfiguration = new PropertyFileBasedLoader().load("base_config.properties");
        assertEquals("str1", baseConfiguration.getString("strProp1"));
        assertEquals("str2", baseConfiguration.getString("strProp2"));
        assertEquals(1, baseConfiguration.getInt("numProp1"));
//        assertEquals(1.0, baseConfiguration.getDouble("numProp1"), 1e-10);
    }

    @Test
    public void testMergeConfiguration() throws Exception {
        RawConfigStorage merged = new LayeredLoader("base_config:layer1_config").load();
        assertEquals("str1", merged.getString("strProp1"));
        assertEquals("str22", merged.getString("strProp2"));
        assertEquals("str32", merged.getString("strProp3"));
        assertEquals(1, merged.getInt("numProp1"));
//        assertEquals(2.2, merged.getDouble("numProp2"), 1e-10);
        assertEquals(1, merged.getInt("numProp1"));
//        assertEquals(1.0, merged.getDouble("numProp1"), 1e-10);
    }

    @Test
    public void testEmptyProperty() throws Exception {
        assertEquals("", new PropertyFileBasedLoader().load("base_config.properties").getString("prop3"));
    }

}
