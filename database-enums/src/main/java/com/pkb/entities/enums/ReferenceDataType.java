package com.pkb.entities.enums;


public enum ReferenceDataType {
    /**
     * In HL7, the order service is typically found in OBR-4. We do not
     * modify the data stored or displayed based on this code set, but
     * we use the values, if provided, to help assign the correct privacy
     * label to the data point.
     */
    ORDER_SERVICE,

    /**
     * In HL7, the hospital service is typically found in PV1-10. This is
     * also referred to as "specialty". This code set is not used to modify
     * the data the we store, but is used to replace the hospital service
     * code with a textual equivalent when displaying in the web interface.
     * In addition, it can help to assign the correct privacy label to data
     * points.
     */
    HOSPITAL_SERVICE,

    /**
     * In HL7, the unit code is typically found in OBX-6. The way we
     * determine which unit value to use is a little convoluted, but if the
     * customer has provided this code set then we might use it to replace
     * the unit values they sent in their message before persisting their
     * data.
     */
    UNIT,

    /**
     * In HL7, the lab discipline is typically found in OBR-24.
     */
    LAB_DISCIPLINE
}