package com.pkb.infrastructure.hibernate.id.enhanced;

import java.io.Serializable;

import org.hibernate.id.enhanced.AccessCallback;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pkb.common.ClearableInternalState;

/**
 * Extend PooledLoOptimizer to track instances and reset internal state (for running tests)
 * Hibernate's PooledLoOptimizer isn't subclassable, so we subclass a tweaked copy of it
 *
 * @see PooledLoOptimizer
 */
public class ResettablePooledLoOptimizer extends PooledLoOptimizerCopy implements ClearableInternalState {

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
        super(returnClass, incrementSize);
    }

    /** reset internal state */
    @Override
    public void clearState() {
        noTenantState = null;
        tenantSpecificState = null;
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

        return super.generate(callback);
    }
}
