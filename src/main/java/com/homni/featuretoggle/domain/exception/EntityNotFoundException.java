package com.homni.featuretoggle.domain.exception;

/**
 * Thrown when an entity is not found by its identifier.
 */
public final class EntityNotFoundException extends DomainNotFoundException {

    /**
     * @param entity the entity type name (e.g. "Toggle", "Project", "User")
     * @param id     the identifier value
     */
    public EntityNotFoundException(String entity, Object id) {
        super("%s [id=%s] not found".formatted(entity, id));
    }
}
