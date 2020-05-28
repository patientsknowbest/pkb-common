package com.pkb.common;

/**
 * Interface for stateful builders.
 *
 * @param <T>
 *            The class which will be built.
 * @param <U>
 *            The builder implementing {@link StatefulBuilder}.
    */
   public interface StatefulBuilder<T, U extends StatefulBuilder<T, U>> {

       /**
        * Call this method when all parameters are set, and new instance should be built.
        *
        * @return Always a new instance.
        */
       T build();
}
