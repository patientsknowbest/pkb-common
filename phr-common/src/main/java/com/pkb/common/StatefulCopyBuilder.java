package com.pkb.common;

/**
 * Interface for stateful builders which can initialize state from an existing result.
 * 
 * @param <T>
 *            The class which will be built.
 * @param <U>
 *            The builder implementing {@link StatefulCopyBuilder}.
 */
public interface StatefulCopyBuilder<T, U extends StatefulCopyBuilder<T, U>> extends StatefulBuilder<T, U> {

    /**
     * Initialize the builder state from the given instance.
     *
     * @param originalInstance
     */
    U initialize(T originalInstance);
}
