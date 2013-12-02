package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;

public interface ProjectAssignmentManagementService {
    /**
     * Assign user to project
     *
     * @param projectAssignment
     */
    ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment);

    /**
     * @param project
     */
    void assignUsersToProjects(Project project);

    /**
     * Assign user to default projects
     *
     * @param user
     * @return
     */
    User assignUserToDefaultProjects(User user);

    /**
     * Delete project assignment
     *
     * @param assignment
     */
    void deleteProjectAssignment(ProjectAssignment assignment);

    /**
     * Update project assignment
     *
     * @param assignment
     */
    void updateProjectAssignment(ProjectAssignment assignment);
}
