package com.homni.featuretoggle.domain.exception;

/**
 * Thrown when a domain invariant is violated during object creation or mutation.
 */
public class DomainValidationException extends DomainException {

    /**
     * Creates a validation exception with the given message.
     *
     * @param message description of the validation failure
     */
    public DomainValidationException(String message) {
        super(message);
    }
}
