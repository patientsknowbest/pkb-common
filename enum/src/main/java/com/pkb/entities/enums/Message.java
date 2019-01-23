//------------------------------------------------------------------------------
//
// Copyright (c) 2009-2013 PatientsKnowBest, Inc. All Rights Reserved.
//
// $Id:  Message.java May 25, 2009 12:20:06 PM mahendera$
//
//------------------------------------------------------------------------------

package com.pkb.entities.enums;

import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

enum MessageCategory {
    THROTTLEABLE,
    REGISTRATION
}

/**
 * This enumeration defines the Message types-
 */
@SuppressWarnings("unused")
public enum Message {

    /** Initial or resent email confirmation on user activation . */
    ACTIVATION(MessageCategory.REGISTRATION),
    EMAIL_VERIFICATION(MessageCategory.REGISTRATION),
    /**/
    INVITE_CLINICIAN(MessageCategory.REGISTRATION),
    /* conversation copying, adding new message to conversation, Instant Medical History Data (IMHM) message, call message, notification to receiver about a patient ? */
    NOTIFICATION_TO_RECEIVER,
    /* not used anywhere ? */
    NOTIFICATION_TO_SENDER,
    /* Invitations/reminders */
    INVITE_INSTITUTE_PATIENT(MessageCategory.REGISTRATION),
    INVITE_INSTITUTE_CLINICIAN(MessageCategory.REGISTRATION),
    INVITE_PATIENT(MessageCategory.REGISTRATION),
    INVITE_INSTITUTE_ADMIN(MessageCategory.REGISTRATION),
    INVITE_REG_CLINICIAN(MessageCategory.REGISTRATION),

    /* patient account created by coord, Emis api or HL7*/
    NOTIFY_PATIENT_OF_CREATED_ACCOUNT(MessageCategory.REGISTRATION),
    /* clinician created by coord, upgrade invited clinician by coord, clinician reminder */
    NOTIFY_CLINICIAN_OF_CREATED_ACCOUNT(MessageCategory.REGISTRATION),
    /* */
    NOTIFICATION_CLINICIAN_ACCEPTED_INVITATION,
    /* upgrade clinician, switch clinician account*/
    NOTIFICATION_TO_INST_ADMIN,

    /* inviteClinician (single & bulk), createOrInvitePatient, invitations (single & bulk)  */
    REQUEST_ACCESS,

    REQUEST_ACCESS_AFTER_REGISTRATION(MessageCategory.REGISTRATION),
    REQUEST_ACCESS_BEFORE_ACTIVATION(MessageCategory.REGISTRATION),

    /*invitation/reminder*/
    INVITE_CLINICIAN_TO_SWITCH_ACCOUNT,
    /*notification to old institute admin*/
    NOTIFICATION_FOR_ACCOUNT_SWITCH,
    /* resend invitation from institute user, invite clinician, remind clinician to upgrade account  */
    INVITE_CLINICIAN_TO_UPGRADE,
    /*request/remind about online consultation */
    INVITE_PATIENT_TO_ONLINE_CONSULTATION,
    /* notification about reject to institute admin */
    NOTIFICATION_FOR_REJECT_ACCOUNT_SWITCH,
    /**/
    RESET_PASSWORD,
    /**/
    REVOKE_ACCESS_NOTIFICATION_TO_PATIENT,
    /**/
    REVOKE_ACCESS_NOTIFICATION_TO_CLINICIAN,
    /*send notification about non-private message to patient's carer*/
    NOTIFICATION_TO_CARER_OF_SENT_MESSAGE,
    /*notify team coord to activate user*/
    ACTIVATION_REQUEST_TO_INST_ADMIN,
    /* notification sent to user after team coord or clinician confirms their identity*/
    ACCOUNT_ACTIVATION_NOTIFICATION,
    /*Unused*/
    NOTIFICATION_OF_PRESCRIPTION(MessageCategory.THROTTLEABLE),
    /*Unused*/
    NOTIFICATION_OF_PRESCRIPTION_WAITING(MessageCategory.THROTTLEABLE),
    /*reminder for patient to document call*/
    PATIENT_DOCUMENT_CALL_REMINDER,
    /*reminder for clinician to document call*/
    CLINICIAN_DOCUMENT_CALL_REMINDER,

    /* notification to new clinician about handing over discussion*/
    HANDOVER_DISCUSSION_NOTIFICATION_TO_NEW_PERSON,
    /* notification to patient about handing over discussion*/
    HANDOVER_DISCUSSION_NOTIFICATION_TO_OTHER_PERSON,
    /* notification to patient about changes made to account by clinical team  */
    ACTIVITY_NOTIFICATION_TO_PATIENT_V2(MessageCategory.THROTTLEABLE),
    /* notification to carer about changes made to account by patient */
    ACTIVITY_NOTIFICATION_TO_CARER(MessageCategory.THROTTLEABLE),
    /* notification to clinician about changes made to account by clinical team  */
    ACTIVITY_NOTIFICATION_TO_CLINICIAN,
    /**/
    INVITE_CLINICIAN_ON_BEHALF_OF_PATIENT(MessageCategory.REGISTRATION),
    /**/
    INVITE_REG_CLINICIAN_ON_BEHALF_OF_PATIENT,
    /**/
    RESET_PASSWORD_BY_SUPER_ADMIN,
    /* confirm new contact address (changed by user, hl7, ...) */
    CONFIRM_NEW_CONTACT,
    /*sent to old and new primary contact address*/
    PRIMARY_CONTACT_CHANGE,
    /**/
    INVITE_CARER,
    /*invite pkb employee*/
    INVITE_EMPLOYEE(MessageCategory.REGISTRATION),
    /*invite new patient*/
    ASK_FOR_ACCESS(MessageCategory.REGISTRATION),
    /*Unused*/
    ASK_FOR_ACCESS_NOTIFICATION(MessageCategory.REGISTRATION),
    /**/
    INVITE_PATIENT_TO_TAKE_SURVEY,
    /*notification to carer*/
    NOTIFICATION_PATIENT_ACCEPTED_OFFERED_CARE,
    /*notification to carer*/
    NOTIFICATION_OFFERED_CARE_ACCEPTED_ON_BEHALF_OF_PATIENT,

    /*notification for patient about granting access to clinician */
    ACCESS_GRANTED_NOTIFICATION_TO_PATIENT,
    /*notification for patient about revoking access from clinician */
    ACCESS_REVOKED_NOTIFICATION_TO_PATIENT,
    /*notification to patient that a third party has revoked access to an individual pro or carer */
    INDIV_ACCESS_REVOKED_NOTIFICATION_TO_PATIENT,
    /*notification for patient's carer about granting access to clinician */
    ACCESS_GRANTED_NOTIFICATION_TO_CARER,
    /*notification for patient's carer about revoking access from clinician */
    ACCESS_REVOKED_NOTIFICATION_TO_CARER,
    /**/
    ACCESS_GRANTED_NOTIFICATION_TO_CLINICIAN,
    /**/
    ACCESS_REVOKED_NOTIFICATION_TO_CLINICIAN,
    /*notification to patient to update symptoms before appointment */
    SYMPTOMS_REMINDER,
    /*notification to user about an unread message (1 day old)*/
    UNREAD_MESSAGE_REMINDER,
    /*notification to user about an unread document (1 day old)*/
    UNREAD_DOCUMENT_REMINDER,
    /**/
    CHILD_BIRTHDAY_REMINDER,
    /**/
    CHILD_BIRTHDAY_REMINDER_TO_COORD,
    /**/
    CHILD_CARER_CONSENT_EXPIRY,
    /**/
    INVITE_TEAM_COORDINATOR(MessageCategory.REGISTRATION),
    /**/
    NOTIFY_COORDINATOR_OF_CREATED_ACCOUNT(MessageCategory.REGISTRATION),
    /**/
    NOTIFY_CLINICIAN_OF_PLAN_AWAITING_APPROVAL,
    /*notification to admin about person joining team*/
    SWITCH_NOTIFICATION_TO_INST_ADMIN,
    /* notification to user about his/her own action - e.g. adding new contact */
    ACTIVITY_NOTIFICATION_TO_SELF(MessageCategory.THROTTLEABLE),
    ACTIVITY_NOTIFICATION_TO_SELF_V2(MessageCategory.THROTTLEABLE),
    /**/
    NOTIFY_TEAM_COORD_TO_UPDATE_SYMPTOM_ALARMS,
    /**/
    NOTIFICATION_TO_COORDINATOR_OF_ACCOUNT_ADDED,
    /**/
    NOTIFY_COORDINATOR_PLAN_EXPORT_COMPLETED,
    /**/
    NOTIFY_CLINICIAN_PLAN_EXPORTED,
    /**/
    NOTIFY_CLINICIAN_FILE_IMPORTED,
    /**/
    INVITE_ORG_COORD(MessageCategory.REGISTRATION),
    /**/
    NOTIFY_COORDINATOR_BULK_ACCOUNT_CREATION_COMPLETED,
    /**/
    NOTIFY_COORDINATOR_BULK_ACCOUNT_CREATION_FAILED,
    /**/
    NOTIFY_COORDINATOR_BULK_CLINICIAN_ACCOUNT_CREATION_COMPLETED,

    // Refactored invitations (May 2015)
    /**/
    PRO_INVITES_CLINICIAN_FOR_PATIENT,
    /**/
    CARER_INVITES_CLINICIAN_FOR_PATIENT,
    /**/
    PATIENT_INVITES_CLINICIAN,
    /*notification to patient*/
    NOTIFICATION_CLINICIAN_ACCEPTED_INVITATION_OBO_PATIENT,
    /**/
    PRO_INVITES_NON_TEAM_CLINICIAN_FOR_PATIENT,
    /**/
    PATIENT_INVITES_NON_TEAM_CARER_FOR_SELF(MessageCategory.REGISTRATION),
    /**/
    CARER_INVITES_NON_TEAM_CARER_FOR_PATIENT(MessageCategory.REGISTRATION),
    /**/
    PRO_INVITES_NON_TEAM_CARER_FOR_PATIENT(MessageCategory.REGISTRATION),
    /**/
    PATIENT_INVITES_TEAM_CARER_FOR_SELF,
    /**/
    CARER_INVITES_TEAM_CARER_FOR_PATIENT,
    /**/
    PRO_INVITES_TEAM_CARER_FOR_PATIENT,
    /**/
    PATIENT_INVITES_REGISTERING_CARER_FOR_SELF,
    /**/
    CARER_INVITES_REGISTERING_CARER_FOR_PATIENT,
    /**/
    PRO_INVITES_REGISTERING_CARER_FOR_PATIENT,
    /**/
    CARER_ACCEPTS_INVITATION_FROM_PATIENT,
    /**/
    CARER_ACCEPTS_INVITATION_FROM_PRO(MessageCategory.THROTTLEABLE),
    /**/
    CARER_ACCEPTS_INVITATION_FROM_OTHER_USER_NOTIFY_PATIENT,


    /* Consent granted to team*/
    TEAM_ACCESS_GRANTED_NOTIFICATION_TO_PATIENT,

    // Org networks
    /* notify org coord of new org network*/
    ORG_NETWORK_CREATED,
    /* notify org coord of invitation to org network*/
    ORG_INVITED_TO_NETWORK,
    /*notifyOrgCoordOfOrgNetworkApproval*/
    ORG_APPROVED_NETWORK,
    /*notifyLeaderOrgCoordOfInvitationRejection*/
    ORG_REJECTED_NETWORK,
    /*notify org coord of org network activation*/
    ORG_ACTIVATED_IN_NETWORK,
    /* currently unused (PHR-1256) */
    ORG_NETWORK_SYNC_COMPLETE,

    /*changePrivacyLevelNotification*/
    NOTIFY_PATIENT_PRIVACY_LEVEL_CHANGED,
    /**/
    NOTIFY_CARER_PRIVACY_LEVEL_CHANGED,

    NOTIFY_PATIENT_DISCHARGE,
    NOTIFY_PATIENT_DISCHARGE_FROM_ORG,
    NOTIFY_CARER_DISCHARGE,

    OTHER_USER_INVITES_CARER_NOTIFY_PATIENT,

    /* invite a privacy officer */
    INVITE_PRIVACY_OFFICER(MessageCategory.REGISTRATION),

    /* notify patient when opting-in or out */
    NOTIFY_PATIENT_SHARING_DISABLED,
    NOTIFY_PATIENT_SHARING_ENABLED,

    /* forgotPassword email notification */
    FORGOT_PASSWORD,

    /* notify patient of HL7 MDM message */
    NOTIFY_PATIENT_OF_DOCUMENT_RECEIVED,

    /* notify carer of HL7 MDM message */
    NOTIFY_CARER_OF_DOCUMENT_RECEIVED_BY_PATIENT,

    /* notification to carer about change made to patient account by clinical team */
    ACTIVITY_NOTIFICATION_TO_CARER_FOR_PATIENT(MessageCategory.THROTTLEABLE),

    /* invitation to patient sent to carer */
    INVITE_TO_CARER_FOR_PATIENT(MessageCategory.REGISTRATION),

    /* notify carer of patient consent granted to team */
    TEAM_ACCESS_GRANTED_NOTIFICATION_TO_CARER_FOR_PATIENT,

    /* notify carer of patient discharged from organisation */
    NOTIFY_CARER_OF_PATIENT_DISCHARGE_FROM_ORG,

    /* invite the carer to take the survey for the patient */
    INVITE_CARER_TO_TAKE_SURVEY_FOR_PATIENT,

    /* notification to carer for the patient about handing over discussion*/
    HANDOVER_DISCUSSION_NOTIFICATION_TO_OTHER_CARER_FOR_PATIENT,

    /* notification to patient about changes made to a privacy label by anyone other than the patient */
    PRIVACY_LABEL_NOTIFICATION_TO_PATIENT,

    /* notification to carer about changes made to a patient privacy label by clinical team */
    PRIVACY_LABEL_NOTIFICATION_TO_CARER,

    /* notification to carer about other user inviting new carer */
    OTHER_USER_INVITES_CARER_NOTIFY_CARER,

    /* notification to carer about created patient account */
    NOTIFY_CARER_OF_CREATED_PATIENT_ACCOUNT,

    /* notification to carer about activation of patient account */
    ACCOUNT_ACTIVATION_CARER_NOTIFICATION,

    /* notification to carer about other carer accepting invitation from other user */
    CARER_ACCEPTS_INVITATION_FROM_OTHER_USER_NOTIFY_CARER,

    /* notification to carer about patient primary contact change */
    PRIMARY_CONTACT_CHANGE_CARER_NOTIFICATION;

    private final Set<MessageCategory> categories;

    Message(MessageCategory... categoryList) {
        categories = Collections.unmodifiableSet(Sets.newEnumSet(Arrays.asList(categoryList), MessageCategory.class));
    }

    /**
     * Determines whether this type of message can be throttled (sending is skipped if recipient received an email today) .
     * For now we don't throttle sharing and discussion notifications .
     * //sharing == invite and/or access ?
     */
    public boolean canBeThrottled() {
        return categories.contains(MessageCategory.THROTTLEABLE);
    }

    public boolean isRegistrationMessage() {
        return categories.contains(MessageCategory.REGISTRATION);
    }
}