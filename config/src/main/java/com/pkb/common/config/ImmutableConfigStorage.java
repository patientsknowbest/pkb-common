package com.pkb.common.config;

public interface ImmutableConfigStorage extends ConfigStorage {

    @Override
    default ImmutableConfigStorage getImmutableConfig() {
        return this;
    }

    @Override
    default boolean isMutableConfigEnabled() {
        return getBoolean(MUTABLE_CONFIG_KEY, false);
    }

    @Override
    default void setValue(String key, String value) {
        /* no-op */
    }

    @Override
    default OverrideRemovalResult removeOverrideAtKey(String key) {
        return OverrideRemovalResult.NO_OP_AS_CONFIG_IS_IMMUTABLE;
    }

    @Override
    default void reset() {
        /* no-op */
    }

}
