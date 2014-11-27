package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;

import java.util.List;

public interface ProjectAssignmentManagementService {
    /**
     * Assign user to project
     */
    ProjectAssignment persistNewProjectAssignment(ProjectAssignment projectAssignment);

    /**
     * Update an existing assignment
     */
    ProjectAssignment persistUpdatedProjectAssignment(ProjectAssignment assignment) throws ProjectAssignmentValidationException;

    /**
     * Assign all users to the specified project
     */
    void assignAllUsersToProject(Project project);

    /**
     * Assign the given uses using the specified assignment as a template
     */
    void assignUsersToProjects(List<User> users, ProjectAssignment assignmentTemplate);

    /**
     * Assign user to default projects
     */
    User assignUserToDefaultProjects(User user);

    /**
     * Delete project assignment
     */
    void deleteProjectAssignment(ProjectAssignment assignment);
}
