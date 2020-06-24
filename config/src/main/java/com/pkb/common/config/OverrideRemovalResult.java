package com.pkb.common.config;

public enum OverrideRemovalResult {
    REMOVED,
    KEY_NOT_FOUND,
    MUTABILITY_KEY_COULD_NOT_BE_REMOVED,
    NO_OP_AS_CONFIG_IS_IMMUTABLE
}
