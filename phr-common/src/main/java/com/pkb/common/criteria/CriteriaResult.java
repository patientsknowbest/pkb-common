package com.pkb.common.criteria;

import static com.pkb.common.criteria.ImmutableCriteriaResult.criteriaResult;

import org.immutables.value.Value;
import org.immutables.value.Value.Style;

/**
 * Represents <em>paginated</em> data.
 */
@Value.Immutable
@Style(of = "criteriaResult")
public interface CriteriaResult<I> extends BaseCriteriaResult<I>{
    static <T> CriteriaResult<T> emptyCriteriaResult() {
        return criteriaResult(0);
    }
}
