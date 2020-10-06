package com.pkb.entities.enums;

// match with Tolven's status values as stored in the DB so migration is simpler.
// Use it in AccountOrg and in AccountUser?
public enum AccountLinkStatus {
    active, inactive, obsolete
}