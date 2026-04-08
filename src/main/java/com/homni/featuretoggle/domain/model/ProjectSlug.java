package com.homni.featuretoggle.domain.model;

import com.homni.featuretoggle.domain.exception.DomainValidationException;

/**
 * Unique short slug identifying a project, used in URLs and configurations.
 *
 * <p>Must be 2-50 uppercase alphanumeric characters, starting with a letter.
 * Hyphens and underscores are allowed. The value is normalized to uppercase.</p>
 *
 * <pre>{@code
 * ProjectSlug slug = new ProjectSlug("my-project");    // stored as "MY-PROJECT"
 * ProjectSlug slug2 = new ProjectSlug("EXAMPLE_ONE_1"); // valid
 * }</pre>
 *
 * @param value the normalized uppercase slug
 */
public record ProjectSlug(String value) {

    private static final String SLUG_PATTERN = "^[A-Z][A-Z0-9_-]*$";

    /**
     * Validates and normalizes the project slug.
     *
     * @throws DomainValidationException if the slug is blank, too short, too long,
     *                                   or does not match the required pattern
     */
    public ProjectSlug {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException("Invalid project slug '%s': must not be blank".formatted(String.valueOf(value)));
        }
        value = value.toUpperCase().trim();
        if (value.length() < 2 || value.length() > 50) {
            throw new DomainValidationException("Invalid project slug '%s': length must be between 2 and 50 characters".formatted(value));
        }
        if (!value.matches(SLUG_PATTERN)) {
            throw new DomainValidationException("Invalid project slug '%s': must start with a letter and contain only letters, digits, hyphens, and underscores".formatted(value));
        }
    }
}
