package com.pkb.infrastructure.hibernate.id.enhanced;

import java.io.Serializable;

import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.PooledLoOptimizer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pkb.common.ClearableInternalState;

/**
 * Extend PooledLoOptimizer to track instances and reset internal state (for running tests)
 */
public class ResettablePooledLoOptimizer implements ClearableInternalState, Optimizer {

    private PooledLoOptimizer optimizer;

    // only used for resetting state (in e2e tests)
    private static final Cache<ClearableInternalState, Boolean> clearableInstances = CacheBuilder.newBuilder()
            .weakKeys()
            .build();
    private boolean lazyAddedToCache = false;

    /**
     * Constructs a ResettablePooledLoOptimizer.
     *
     * @param returnClass The Java type of the values to be generated
     * @param incrementSize The increment size.
     */
    public ResettablePooledLoOptimizer(Class returnClass, int incrementSize) {
        this.optimizer = newOptimizer(returnClass, incrementSize);
    }

    private PooledLoOptimizer newOptimizer(Class returnClass, int incrementSize) {
        return new PooledLoOptimizer(returnClass, incrementSize);
    }

    @Override
    public void clearState() {
        this.optimizer = newOptimizer(optimizer.getReturnClass(), optimizer.getIncrementSize());
    }

    public static void clearStateGlobal() {
        for (ClearableInternalState instance : clearableInstances.asMap().keySet()) {
            instance.clearState();
        }
    }

    @Override
    public synchronized Serializable generate(AccessCallback callback) {
        if (!lazyAddedToCache) {
            //noinspection AccessToStaticFieldLockedOnInstance
            clearableInstances.put(this, Boolean.TRUE);
            lazyAddedToCache = true;
        }
        return optimizer.generate(callback);
    }

    @Override
    public boolean applyIncrementSizeToSourceValues() {
        return optimizer.applyIncrementSizeToSourceValues();
    }

    @Override
    public final int getIncrementSize() {
        return optimizer.getIncrementSize();
    }

    @Override
    public IntegralDataTypeHolder getLastSourceValue() {
        return optimizer.getLastSourceValue();
    }

    /**
     * Getter for property 'returnClass'.  This is the Java
     * class which is used to represent the id (e.g. {@link Long}).
     *
     * @return Value for property 'returnClass'.
     */
    @SuppressWarnings({"UnusedDeclaration"})
    public final Class getReturnClass() {
        return optimizer.getReturnClass();
    }
}