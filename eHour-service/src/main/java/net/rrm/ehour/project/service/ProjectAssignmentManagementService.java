package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;

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
     * @param assignmentId
     */
    void deleteProjectAssignment(Integer assignmentId) throws ObjectNotFoundException, ParentChildConstraintException;


    /**
     * Update project assignment
     *
     * @param assignment
     */
    void updateProjectAssignment(ProjectAssignment assignment);
}
