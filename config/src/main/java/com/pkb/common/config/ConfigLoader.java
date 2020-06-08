package com.pkb.common.config;

import java.util.function.Supplier;

@FunctionalInterface
interface ConfigLoader extends Supplier<ImmutableRawConfigStorage> {

    ImmutableRawConfigStorage load();

    @Override
    default ImmutableRawConfigStorage get() {
        return load();
    }
}
