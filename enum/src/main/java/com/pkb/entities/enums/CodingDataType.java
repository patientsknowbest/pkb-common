package com.pkb.entities.enums;

/**
 * The type of the coding inside PKB -- in terms of the PKB data model.
 * Not the same as a data point type (allergy) but each type of coding we need to filter/query on inside our data points.
 *
 * @author robwhelan
 */
public enum CodingDataType {
    ALLERGEN, ALLERGY_DISORDER, DIAGNOSIS, MEDICATION, UNMAPPED, SYMPTOM
}
