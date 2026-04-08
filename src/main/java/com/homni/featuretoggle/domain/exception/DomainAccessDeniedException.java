package com.homni.featuretoggle.domain.exception;

/**
 * Thrown when a domain operation is denied due to insufficient access rights.
 */
public class DomainAccessDeniedException extends DomainException {

    /**
     * Creates an access-denied exception with the given message.
     *
     * @param message description of the denied access
     */
    public DomainAccessDeniedException(String message) {
        super(message);
    }
}
