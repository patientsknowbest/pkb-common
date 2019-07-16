package com.pkb.infrastructure.hibernate.id.enhanced;

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.hibernate.HibernateException;
import org.hibernate.id.IntegralDataTypeHolder;
import org.hibernate.id.enhanced.AccessCallback;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;


/**
 * Copy -- only changed to make subclassable
 * Needed to merge in AbstractOptimizer... with its package-private constructor :|
 *
 * @author Steve Ebersole
 *
 * @see Hibernate's PooledLoOptimizer
 */
public class PooledLoOptimizerCopy implements Optimizer {
    // from AbstractOptimizer
    protected final Class returnClass;
    protected final int incrementSize;

    /**
     * Construct an optimizer
     *
     * @param returnClass The expected id class.
     * @param incrementSize The increment size
     */
//    impl copied into ctor below
//    AbstractOptimizer(Class returnClass, int incrementSize) {
//        if ( returnClass == null ) {
//            throw new HibernateException( "return class is required" );
//        }
//        this.returnClass = returnClass;
//        this.incrementSize = incrementSize;
//    }

    /**
     * Getter for property 'returnClass'.  This is the Java
     * class which is used to represent the id (e.g. {@link Long}).
     *
     * @return Value for property 'returnClass'.
     */
    @SuppressWarnings( {"UnusedDeclaration"})
    public final Class getReturnClass() {
        return returnClass;
    }

    @Override
    public final int getIncrementSize() {
        return incrementSize;
    }
    // ----------------------------------

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(
            CoreMessageLogger.class,
            PooledLoOptimizerCopy.class.getName()
    );

    private static class GenerationState {
        // last value read from db source
        private IntegralDataTypeHolder lastSourceValue;
        // the current generator value
        private IntegralDataTypeHolder value;
        // the value at which we'll hit the db again
        private IntegralDataTypeHolder upperLimitValue;
    }

    /**
     * Constructs a PooledLoOptimizer.
     *
     * @param returnClass The Java type of the values to be generated
     * @param incrementSize The increment size.
     */
    public PooledLoOptimizerCopy(Class returnClass, int incrementSize) {
        // from AbstractOptimizer
        if ( returnClass == null ) {
            throw new HibernateException( "return class is required" );
        }
        this.returnClass = returnClass;
        this.incrementSize = incrementSize;
        // -----------------------

        if ( incrementSize < 1 ) {
            throw new HibernateException( "increment size cannot be less than 1" );
        }
        LOG.creatingPooledLoOptimizer( incrementSize, returnClass.getName() );
    }

    @Override
    public synchronized Serializable generate(AccessCallback callback) {
        GenerationState generationState = locateGenerationState( callback.getTenantIdentifier() );

        if ( generationState.lastSourceValue == null
                || ! generationState.value.lt( generationState.upperLimitValue ) ) {
            generationState.lastSourceValue = callback.getNextValue();
            generationState.upperLimitValue = generationState.lastSourceValue.copy().add( incrementSize );
            generationState.value = generationState.lastSourceValue.copy();
            // handle cases where initial-value is less that one (hsqldb for instance).
            while ( generationState.value.lt( 1 ) ) {
                generationState.value.increment();
            }
        }
        return generationState.value.makeValueThenIncrement();
    }

    // change private to protected
    protected GenerationState noTenantState;
    protected Map<String,GenerationState> tenantSpecificState;

    private GenerationState locateGenerationState(String tenantIdentifier) {
        if ( tenantIdentifier == null ) {
            if ( noTenantState == null ) {
                noTenantState = new GenerationState();
            }
            return noTenantState;
        }
        else {
            GenerationState state;
            if ( tenantSpecificState == null ) {
                tenantSpecificState = new ConcurrentHashMap<>();
                state = new GenerationState();
                tenantSpecificState.put( tenantIdentifier, state );
            }
            else {
                state = tenantSpecificState.computeIfAbsent(tenantIdentifier, k -> new GenerationState());
            }
            return state;
        }
    }

    private GenerationState noTenantGenerationState() {
        if ( noTenantState == null ) {
            throw new IllegalStateException( "Could not locate previous generation state for no-tenant" );
        }
        return noTenantState;
    }

    @Override
    public IntegralDataTypeHolder getLastSourceValue() {
        return noTenantGenerationState().lastSourceValue;
    }

    @Override
    public boolean applyIncrementSizeToSourceValues() {
        return true;
    }
}