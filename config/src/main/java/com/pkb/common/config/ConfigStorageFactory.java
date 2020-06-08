package com.pkb.common.config;

public final class ConfigStorageFactory {

    private ConfigStorageFactory() {
    }

    public static ConfigStorage getConfigStorage() {
        ImmutableRawConfigStorage immutableRawConfigStorage = ImmutableRawConfigStorage.createDefault();
        if (immutableRawConfigStorage.isMutableConfigEnabled()) {
            return new MutableRawConfigStorage(immutableRawConfigStorage);
        }
        return immutableRawConfigStorage;
    }
}
