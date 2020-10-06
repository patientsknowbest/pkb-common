package com.pkb.entities.enums;

/**
 * Matches PSQL enum type doc_storage_backend_type
 */
public enum DocStorageBackend {
    DOC,
    DOC_NEARLINE,
    IN_MEMORY,
    GCS,
    FIRESTORE
}

