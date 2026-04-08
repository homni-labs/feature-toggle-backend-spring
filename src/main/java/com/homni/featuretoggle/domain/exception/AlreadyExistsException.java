package com.homni.featuretoggle.domain.exception;

/**
 * Thrown when attempting to create an entity that already exists (uniqueness violation).
 */
public final class AlreadyExistsException extends DomainConflictException {

    /**
     * @param entity     the entity type name (e.g. "Project", "Toggle", "Membership")
     * @param identifier the conflicting identifier value
     */
    public AlreadyExistsException(String entity, String identifier) {
        super("%s '%s' already exists".formatted(entity, identifier));
    }
}
