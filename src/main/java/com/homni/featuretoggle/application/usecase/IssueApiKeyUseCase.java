package com.homni.featuretoggle.application.usecase;

import com.homni.featuretoggle.application.port.out.ApiKeyRepositoryPort;
import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.domain.exception.EntityNotFoundException;
import com.homni.featuretoggle.domain.exception.ProjectArchivedException;
import com.homni.featuretoggle.domain.model.IssuedApiKey;
import com.homni.featuretoggle.domain.model.Permission;
import com.homni.featuretoggle.domain.model.Project;
import com.homni.featuretoggle.domain.model.ProjectId;

import java.time.Instant;

/**
 * Issues a new read-only API key bound to a project.
 * API keys always grant READER role for SDK/machine access.
 */
public final class IssueApiKeyUseCase {

    private final ApiKeyRepositoryPort apiKeys;
    private final ProjectRepositoryPort projects;
    private final CallerProjectAccessPort callerAccess;

    /**
     * Creates an issue-api-key use case.
     *
     * @param apiKeys      the API key persistence port
     * @param projects     the project persistence port
     * @param callerAccess resolves the caller's project access
     */
    public IssueApiKeyUseCase(ApiKeyRepositoryPort apiKeys,
                               ProjectRepositoryPort projects,
                               CallerProjectAccessPort callerAccess) {
        this.apiKeys = apiKeys;
        this.projects = projects;
        this.callerAccess = callerAccess;
    }

    /**
     * Issues a new read-only API key for the specified project.
     *
     * @param projectId the owning project identity
     * @param name      the key name (1-255 non-blank characters)
     * @param expiresAt the expiration timestamp, may be {@code null} for no expiration
     * @return the issued API key containing both the persisted key and the raw token
     * @throws com.homni.featuretoggle.domain.exception.InsufficientPermissionException if access lacks MANAGE_MEMBERS
     * @throws ProjectArchivedException if the project is archived
     *
     * <pre>{@code
     * IssuedApiKey issued = issueApiKey.execute(projectId, "ci-pipeline", null);
     * }</pre>
     */
    public IssuedApiKey execute(ProjectId projectId, String name, Instant expiresAt) {
        callerAccess.resolve(projectId).ensure(Permission.MANAGE_MEMBERS);
        ensureProjectNotArchived(projectId);
        IssuedApiKey issued = new IssuedApiKey(projectId, name, expiresAt);
        apiKeys.save(issued.apiKey);
        return issued;
    }

    private void ensureProjectNotArchived(ProjectId projectId) {
        Project project = projects.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project", projectId.value));
        if (project.isArchived()) {
            throw new ProjectArchivedException(projectId);
        }
    }
}
