package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectRole;

/**
 * Pairs a project with the caller's role in that project.
 *
 * <p>{@code myRole} is the user's project role (e.g. ADMIN, EDITOR, READER),
 * or {@code null} when the caller is a platform admin viewing all projects.</p>
 *
 * @param project the project
 * @param myRole  the caller's role, or {@code null} for platform administrators
 */
public record ProjectWithRole(Project project, ProjectRole myRole) {}
