package com.pkb.common.error;

/**
 * These would be helpful to document the error codes in user guide so that
 * end-user could try and recover in few exception condition 
 * 
 * E.g if NO_VALID_SSO_AUTH it means that either user hasn't generated SSO credentials
 * or hasn't configured the external system with newly generated credentials
 * 
 * @author pravina
 *
 */
public class SSOException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5188967730485194183L;

    public enum ErrorCode {

        BAD_SSO_KEY(0, "SSO key supplied by external system is invalid"), 
        NO_VALID_SSO_AUTH(1, "No valid SSO auth record found "),
        PRIVATE_KEY_DECRYPTION_ERROR(2, "Private key decryption error; SSO credentials requires reset"),
        SSO_AUTH_ENCRYPTION_ERROR(3, "Private key encryption error; SSO credentials requires reset"),
        NO_SSO_ID_LINK(4, "No valid SSO Id Link object found for sso username");

        private final int code;
        private final String description;

        ErrorCode(int code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            return code + ": " + description;
        }

    }
    
    private ErrorCode errorCode;
    
    public SSOException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SSOException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
}
