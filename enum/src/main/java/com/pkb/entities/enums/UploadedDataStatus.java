package com.pkb.entities.enums;

public enum UploadedDataStatus {
    /** Data is ready for processing. */
    NEW,
    /** Data has been claimed and is being processed. */
    ACTIVE,
    /** Data has been processed. */
    COMPLETED,
    /** ??? */
    OBSOLETE,
    /** Soft demographics check failed; this data needs manually approval before it will be processed. */
    PATIENT_INFO_MISMATCH,
    /** The data was not processed, e.g. because it was invalid. */
    REJECTED
}
