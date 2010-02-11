package net.rrm.ehour.project.service;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;

public interface ProjectAssignmentManagementService
{
	
	
	
	/**
	 * Assign user to project
	 * @param projectAssignment
	 */
	public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment);
	
	/**
	 * 
	 * @param project
	 */
	public void assignUsersToProjects(Project project);
	
	/**
	 * Assign user to default projects
	 * @param user
	 * @return
	 */
	public User assignUserToDefaultProjects(User user);
	
	/**
	 * Delete project assignment
	 * @param assignmentId
	 */
	public void deleteProjectAssignment(Integer assignmentId) throws ObjectNotFoundException, ParentChildConstraintException;
	
	
	/**
	 * Update project assignment
	 * @param assignment
	 */
	public void updateProjectAssignment(ProjectAssignment assignment);
}
