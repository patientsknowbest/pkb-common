package com.pkb.common.criteria;

import org.immutables.value.Value;
import org.immutables.value.Value.Style;

@Value.Immutable
@Style(of = "restriction", allParameters = true)
public interface Restriction<C, V> {

    C getConstraint();

    V getValue();
}
