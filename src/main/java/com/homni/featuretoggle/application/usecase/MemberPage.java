package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.domain.model.ProjectMembership;

import java.util.List;
import java.util.Objects;

/**
 * A page of project memberships with pagination metadata.
 *
 * @param items         the memberships on this page
 * @param totalElements total count across all pages
 */
public record MemberPage(List<ProjectMembership> items, long totalElements) {

    /**
     * Creates a page with a defensive copy of the items list.
     */
    public MemberPage {
        Objects.requireNonNull(items, "items must not be null");
        items = List.copyOf(items);
    }
}
