package com.pkb.entities.enums;

public enum Route {
    // direct access into PKB (pushing data or reading)
    WEBAPP,
    WEBAPP_EMIS_PORTAL, // subcategory of webapp access
    WEBAPP_OTP,
    REST_API,
    HL7_API,
    CSV_API,    // PoC phase; not yet in use in production

    // data pulled from external sources
    CORE_DEVICES,
    EMIS_ES,
    EMIS_PFS,
    SCISTORE,
    PACR,
    S1,         // rename for specific integration before using in production

    SCHED_TASK, // almost never a source of data but may read data and thus show up in audit logs
    EXPORT,     // data collection, e.g., careplan exports -- may show up in audit log

    NOT_CAPTURED; // old data

    public boolean isCaptured(){
        return this != NOT_CAPTURED;
    }
}
