package com.pkb.entities.enums;


import static com.github.karsaig.approvalcrest.MatcherAssert.assertThat;
import static com.github.karsaig.approvalcrest.matcher.Matchers.sameContentAsApproved;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import com.pkb.entities.enums.MeasurementType.Category;

//7f8670
public class MeasurementTypeTest {

    @Test
    public void testForMeasurementComments() {
        StringBuilder actualBuilder = new StringBuilder();
        actualBuilder.append("Category,ApiRef\n");
        for (Category c : Category.values()) {
            for (MeasurementType m : MeasurementType.values()) {
                if (m.getCategory() == c) {
                    actualBuilder.append(c + "," + m.getApiRef() + "\n");
                }
            }
        }

        //f260b5
        assertThat(
                "IMPORTANT: these measurements are part of the REST API, documented via the COMMENTS in the ApiMeasurement class. After updating the test, update the comments!",
                actualBuilder.toString(), sameContentAsApproved());
    }


    /**
     * Duplicated IDs would be bad
     */
    @Test
    public void checkNoDuplicateIds() {
        Long[] duplicateIds = Arrays.stream(MeasurementType.values())
                .collect(Collectors.groupingBy(MeasurementType::getId))
                .entrySet().stream()
                .filter(it -> it.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toArray(Long[]::new);

        assertEquals("Found duplicate measurement type IDs " + Arrays.toString(duplicateIds), 0, duplicateIds.length);
    }

    @Test
    public void checkNoDuplicateApiRefs() {
        String[] duplicateApiRefs = Arrays.stream(MeasurementType.values())
                .collect(Collectors.groupingBy(MeasurementType::getApiRef))
                .entrySet().stream()
                .filter(it -> it.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .toArray(String[]::new);

        assertEquals("Found duplicate MeasurementType.apiRefs " + Arrays.toString(duplicateApiRefs), 0, duplicateApiRefs.length);
    }
}
