package com.pkb.common.csv;

import java.util.List;
import java.util.Set;

import com.pkb.common.base.Error;

public interface HeaderValidator {
    List<Error> validateHeader(Set<String> actualColumnHeaders);
}
