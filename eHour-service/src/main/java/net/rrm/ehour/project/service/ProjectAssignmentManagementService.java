package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;

import java.util.List;

public interface ProjectAssignmentManagementService {
    /**
     * Assign user to project
     *
     * @param projectAssignment
     */
    ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment);

    /**
     * Assign all users to the specified project
     * @param project
     */
    void assignAllUsersToProject(Project project);

    /**
     * Assign the given uses using the specified assignment as a template
     * @param users
     * @param assignmentTemplate
     */
    void assignUsersToProjects(List<User> users, ProjectAssignment assignmentTemplate);

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
