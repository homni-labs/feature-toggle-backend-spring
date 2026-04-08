package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectSlug;

/**
 * Creates a new project with the given key, name, and description.
 */
public final class CreateProjectUseCase {

    private final ProjectRepositoryPort projects;

    /**
     * Creates a create-project use case.
     *
     * @param projects the project persistence port
     */
    public CreateProjectUseCase(ProjectRepositoryPort projects) {
        this.projects = projects;
    }

    /**
     * Creates a new project and persists it.
     *
     * @param key         the unique project key
     * @param name        the human-readable project name
     * @param description the project description, may be {@code null}
     * @return the created project
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if the key is invalid
     * @throws com.homni.featuretoggle.domain.exception.DomainValidationException if the name is invalid
     *
     * <pre>{@code
     * Project project = createProject.execute(new ProjectSlug("MY-APP"), "My App", "Main backend");
     * }</pre>
     */
    public Project execute(ProjectSlug slug, String name, String description) {
        Project project = new Project(slug, name, description);
        projects.save(project);
        return project;
    }
}
