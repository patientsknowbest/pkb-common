package com.pkb.common.config;

import static java.lang.invoke.MethodHandles.lookup;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

public class LayeredLoader implements ConfigLoader {
    private static final Logger LOGGER = getLogger(lookup().lookupClass());

    private static final String PKB_SERVICE_CONFIG = "PKB_SERVICE_CONFIG";

    static final String DEFAULT_CONFIG_FILE_PATH = "config/default";

    public static LayeredLoader defaultLoader() {
        return createLoaderForEnvVar(System.getenv(PKB_SERVICE_CONFIG));
    }

    public static LayeredLoader createLoaderForEnvVar(String colonSeparatedFileList) {
        List<ConfigLoader> layerLoaders = mapEnvVarValueToPropertyFileList(colonSeparatedFileList).stream()
                .map(PropertyFileBasedLoader::new)
                .collect(toList());
        return new LayeredLoader(layerLoaders);
    }

    static List<String> mapEnvVarValueToPropertyFileList(String colonSeparatedFileList) {
        List<String> propertyFiles;
        if (colonSeparatedFileList == null) {
            propertyFiles = singletonList(DEFAULT_CONFIG_FILE_PATH);
        } else {
            String[] filePaths = colonSeparatedFileList.split(":");
            if (filePaths[0].trim().equals(DEFAULT_CONFIG_FILE_PATH)) {
                propertyFiles = asList(filePaths);
            } else {
                propertyFiles = new ArrayList<>(filePaths.length + 1);
                propertyFiles.add(DEFAULT_CONFIG_FILE_PATH);
                propertyFiles.addAll(asList(filePaths));
            }
        }
        return propertyFiles.stream()
                .map(filePath -> filePath + ".properties")
                .peek(path -> LOGGER.info("Will load property from path=[{}]", path))
                .collect(toList());
    }

    private final List<ConfigLoader> layerLoaders;

    public LayeredLoader(List<ConfigLoader> layerLoaders) {
        this.layerLoaders = unmodifiableList(layerLoaders);
    }

    @Override
    public ImmutableRawConfigStorage load() {
        return layerLoaders.stream()
                .map(ConfigLoader::load)
                .reduce(ImmutableRawConfigStorage.EMPTY, ImmutableRawConfigStorage::merge);
    }

}
