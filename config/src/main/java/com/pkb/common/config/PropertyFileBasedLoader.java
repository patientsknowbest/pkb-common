package com.pkb.common.config;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyFileBasedLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFileBasedLoader.class);

    public RawConfigStorage load(String propFilePath) {
        Properties props = new Properties();
        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFilePath);
        try (InputStream stream = inputStreamOfPropertyFile(propFilePath)){
            props.load(stream);
            Map<String, String> storage = new HashMap<>(props.size());
            props.forEach((k, v) -> storage.put((String) k, (String) v));
            return new RawConfigStorage(storage);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private InputStream inputStreamOfPropertyFile(String propFile) throws IOException {
        File file = new File(propFile);
        if (file.exists()) {
            return new FileInputStream(file);
        }
        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFile);
        if (resource == null) {
            LOGGER.warn("failed to look up config file {}", propFile);
            throw new ConfigurationException(format("could not find property file [%s]", propFile));
        }
        return resource.openStream();
    }
}
