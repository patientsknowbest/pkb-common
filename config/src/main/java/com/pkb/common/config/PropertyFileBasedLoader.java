package com.pkb.common.config;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFileBasedLoader implements ConfigLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileBasedLoader.class);

    private final String propFilePath;

    public PropertyFileBasedLoader(@NotNull String propFilePath) {
        this.propFilePath = propFilePath;
    }

    @Override
    public RawConfigStorage load() {
        try {
            PropertiesConfiguration c = new PropertiesConfiguration(propFilePath);
            Iterator<String> it = c.getKeys();
            Map<String, String> storage = new HashMap<>();
            it.forEachRemaining(key -> storage.put(key, c.getProperty(key).toString()));
            return new RawConfigStorage(storage);
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            e.printStackTrace();
            throw new ConfigurationException(e.getMessage());
        }
//        Properties props = new Properties();
//        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFilePath);
//        try (InputStream stream = inputStreamOfPropertyFile(propFilePath)){
//            props.load(stream);
//            Map<String, String> storage = new HashMap<>(props.size());
//            props.forEach((k, v) -> storage.put((String) k, (String) v));
//            return new RawConfigStorage(storage);
//        } catch (IOException e) {
//            throw new UncheckedIOException(e);
//        }
    }

    private InputStream inputStreamOfPropertyFile(String propFile) throws IOException {
        File file = new File(propFile);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFile);
        if (resource == null) {
            resource = ClassLoader.getSystemResource(propFile);
        }
        if (resource == null) {
            LOGGER.warn("failed to look up config file {}", propFile);
            throw new ConfigurationException(format("could not find property file [%s]", propFile));
        }
        return resource.openStream();
    }
}
