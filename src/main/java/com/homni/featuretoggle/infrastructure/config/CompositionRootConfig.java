package com.homni.featuretoggle.infrastructure.config;

import com.homni.featuretoggle.application.port.out.ApiKeyRepositoryPort;
import com.homni.featuretoggle.application.port.out.AppUserRepositoryPort;
import com.homni.featuretoggle.application.port.out.CallerPort;
import com.homni.featuretoggle.application.port.out.CallerProjectAccessPort;
import com.homni.featuretoggle.application.port.out.EnvironmentRepositoryPort;
import com.homni.featuretoggle.application.port.out.FeatureToggleRepositoryPort;
import com.homni.featuretoggle.application.port.out.ProjectMembershipRepositoryPort;
import com.homni.featuretoggle.application.port.out.ProjectRepositoryPort;
import com.homni.featuretoggle.application.usecase.CreateEnvironmentUseCase;
import com.homni.featuretoggle.application.usecase.CreateProjectUseCase;
import com.homni.featuretoggle.application.usecase.CreateToggleUseCase;
import com.homni.featuretoggle.application.usecase.DeleteEnvironmentUseCase;
import com.homni.featuretoggle.application.usecase.DeleteToggleUseCase;
import com.homni.featuretoggle.application.usecase.FindOrCreateUserUseCase;
import com.homni.featuretoggle.application.usecase.FindToggleUseCase;
import com.homni.featuretoggle.application.usecase.GetCurrentUserUseCase;
import com.homni.featuretoggle.application.usecase.IssueApiKeyUseCase;
import com.homni.featuretoggle.application.usecase.ListApiKeysUseCase;
import com.homni.featuretoggle.application.usecase.ListEnvironmentsUseCase;
import com.homni.featuretoggle.application.usecase.ListMembersUseCase;
import com.homni.featuretoggle.application.usecase.ListProjectsUseCase;
import com.homni.featuretoggle.application.usecase.ListTogglesUseCase;
import com.homni.featuretoggle.application.usecase.ListUsersUseCase;
import com.homni.featuretoggle.application.usecase.RemoveMemberUseCase;
import com.homni.featuretoggle.application.usecase.ResolveProjectAccessUseCase;
import com.homni.featuretoggle.application.usecase.RevokeApiKeyUseCase;
import com.homni.featuretoggle.application.usecase.SearchUsersUseCase;
import com.homni.featuretoggle.application.usecase.UpdateProjectUseCase;
import com.homni.featuretoggle.application.usecase.UpdateToggleUseCase;
import com.homni.featuretoggle.application.usecase.UpdateUserUseCase;
import com.homni.featuretoggle.application.usecase.UpsertMemberUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central wiring configuration that creates all use-case beans,
 * connecting output ports (adapters) to application-layer orchestrators.
 */
@Configuration
class CompositionRootConfig {

    // --- Toggle use-cases ---

    /**
     * Creates a toggle within a project after validating environments.
     *
     * @param toggles      the toggle persistence port
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    CreateToggleUseCase createToggleUseCase(FeatureToggleRepositoryPort toggles,
                                            EnvironmentRepositoryPort environments,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new CreateToggleUseCase(toggles, environments, projects, callerAccess);
    }

    /**
     * Finds a single toggle by identity, verifying read access.
     *
     * @param toggles      the toggle persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    FindToggleUseCase findToggleUseCase(FeatureToggleRepositoryPort toggles,
                                        CallerProjectAccessPort callerAccess) {
        return new FindToggleUseCase(toggles, callerAccess);
    }

    /**
     * Lists toggles for a project with filtering and pagination.
     *
     * @param toggles      the toggle persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    ListTogglesUseCase listTogglesUseCase(FeatureToggleRepositoryPort toggles,
                                          CallerProjectAccessPort callerAccess) {
        return new ListTogglesUseCase(toggles, callerAccess);
    }

    /**
     * Updates a toggle's mutable fields.
     *
     * @param toggles      the toggle persistence port
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    UpdateToggleUseCase updateToggleUseCase(FeatureToggleRepositoryPort toggles,
                                            EnvironmentRepositoryPort environments,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new UpdateToggleUseCase(toggles, environments, projects, callerAccess);
    }

    /**
     * Deletes a toggle from a project.
     *
     * @param toggles      the toggle persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    DeleteToggleUseCase deleteToggleUseCase(FeatureToggleRepositoryPort toggles,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new DeleteToggleUseCase(toggles, projects, callerAccess);
    }

    // --- Project use-cases ---

    /**
     * Creates a new project.
     *
     * @param projects the project persistence port
     * @return the wired use case
     */
    @Bean
    CreateProjectUseCase createProjectUseCase(ProjectRepositoryPort projects) {
        return new CreateProjectUseCase(projects);
    }

    /**
     * Lists projects visible to the calling user.
     *
     * @param projects   the project persistence port
     * @param callerPort provides the authenticated caller
     * @return the wired use case
     */
    @Bean
    ListProjectsUseCase listProjectsUseCase(ProjectRepositoryPort projects,
                                             CallerPort callerPort) {
        return new ListProjectsUseCase(projects, callerPort);
    }

    /**
     * Updates a project's mutable fields.
     *
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    UpdateProjectUseCase updateProjectUseCase(ProjectRepositoryPort projects,
                                              CallerProjectAccessPort callerAccess) {
        return new UpdateProjectUseCase(projects, callerAccess);
    }

    // --- User use-cases ---

    /**
     * Resolves or creates a user during OIDC login.
     *
     * @param users             the user persistence port
     * @param defaultAdminEmail the default admin email from configuration
     * @return the wired use case
     */
    @Bean
    FindOrCreateUserUseCase findOrCreateUserUseCase(
            AppUserRepositoryPort users,
            @Value("${app.oidc.default-admin-email:}") String defaultAdminEmail) {
        return new FindOrCreateUserUseCase(users, defaultAdminEmail);
    }

    /**
     * Returns the currently authenticated user.
     *
     * @param callerPort provides the authenticated caller
     * @return the wired use case
     */
    @Bean
    GetCurrentUserUseCase getCurrentUserUseCase(CallerPort callerPort) {
        return new GetCurrentUserUseCase(callerPort);
    }

    /**
     * Lists all platform users with pagination.
     *
     * @param users the user persistence port
     * @return the wired use case
     */
    @Bean
    ListUsersUseCase listUsersUseCase(AppUserRepositoryPort users) {
        return new ListUsersUseCase(users);
    }

    /**
     * Searches users by email or name substring.
     *
     * @param users the user persistence port
     * @return the wired use case
     */
    @Bean
    SearchUsersUseCase searchUsersUseCase(AppUserRepositoryPort users) {
        return new SearchUsersUseCase(users);
    }

    /**
     * Updates a user's platform role and active status.
     *
     * @param users      the user persistence port
     * @param callerPort provides the authenticated caller
     * @return the wired use case
     */
    @Bean
    UpdateUserUseCase updateUserUseCase(AppUserRepositoryPort users, CallerPort callerPort) {
        return new UpdateUserUseCase(users, callerPort);
    }

    // --- API Key use-cases ---

    /**
     * Issues a new API key bound to a project.
     *
     * @param apiKeys      the API key persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    IssueApiKeyUseCase issueApiKeyUseCase(ApiKeyRepositoryPort apiKeys,
                                          ProjectRepositoryPort projects,
                                          CallerProjectAccessPort callerAccess) {
        return new IssueApiKeyUseCase(apiKeys, projects, callerAccess);
    }

    /**
     * Lists API keys for a project with pagination.
     *
     * @param apiKeys      the API key persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    ListApiKeysUseCase listApiKeysUseCase(ApiKeyRepositoryPort apiKeys,
                                          CallerProjectAccessPort callerAccess) {
        return new ListApiKeysUseCase(apiKeys, callerAccess);
    }

    /**
     * Revokes an API key within a project.
     *
     * @param apiKeys      the API key persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    RevokeApiKeyUseCase revokeApiKeyUseCase(ApiKeyRepositoryPort apiKeys,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new RevokeApiKeyUseCase(apiKeys, projects, callerAccess);
    }

    // --- Environment use-cases ---

    /**
     * Creates a new environment within a project.
     *
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    CreateEnvironmentUseCase createEnvironmentUseCase(EnvironmentRepositoryPort environments,
                                                      ProjectRepositoryPort projects,
                                                      CallerProjectAccessPort callerAccess) {
        return new CreateEnvironmentUseCase(environments, projects, callerAccess);
    }

    /**
     * Lists all environments for a project.
     *
     * @param environments the environment persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    ListEnvironmentsUseCase listEnvironmentsUseCase(EnvironmentRepositoryPort environments,
                                                    CallerProjectAccessPort callerAccess) {
        return new ListEnvironmentsUseCase(environments, callerAccess);
    }

    /**
     * Deletes an environment from a project.
     *
     * @param environments the environment persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    DeleteEnvironmentUseCase deleteEnvironmentUseCase(EnvironmentRepositoryPort environments,
                                                      ProjectRepositoryPort projects,
                                                      CallerProjectAccessPort callerAccess) {
        return new DeleteEnvironmentUseCase(environments, projects, callerAccess);
    }

    // --- Member use-cases ---

    /**
     * Adds a user to a project or updates their role (upsert).
     *
     * @param memberships  the membership persistence port
     * @param users        the user persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    UpsertMemberUseCase upsertMemberUseCase(ProjectMembershipRepositoryPort memberships,
                                            AppUserRepositoryPort users,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new UpsertMemberUseCase(memberships, users, projects, callerAccess);
    }

    /**
     * Lists project members with pagination.
     *
     * @param memberships  the membership persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    ListMembersUseCase listMembersUseCase(ProjectMembershipRepositoryPort memberships,
                                          CallerProjectAccessPort callerAccess) {
        return new ListMembersUseCase(memberships, callerAccess);
    }

    /**
     * Removes a member from a project.
     *
     * @param memberships  the membership persistence port
     * @param projects     the project persistence port
     * @param callerAccess the caller project access port
     * @return the wired use case
     */
    @Bean
    RemoveMemberUseCase removeMemberUseCase(ProjectMembershipRepositoryPort memberships,
                                            ProjectRepositoryPort projects,
                                            CallerProjectAccessPort callerAccess) {
        return new RemoveMemberUseCase(memberships, projects, callerAccess);
    }

    // --- Access resolution ---

    /**
     * Resolves a user's access level for a specific project.
     *
     * @param memberships the membership persistence port
     * @return the wired use case
     */
    @Bean
    ResolveProjectAccessUseCase resolveProjectAccessUseCase(
            ProjectMembershipRepositoryPort memberships) {
        return new ResolveProjectAccessUseCase(memberships);
    }
}
