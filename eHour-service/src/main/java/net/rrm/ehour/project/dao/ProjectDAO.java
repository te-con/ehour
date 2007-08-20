package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.user.domain.User;

/**
 * CRUD on project domain object
 * @author Thies
 *
 */

public interface ProjectDAO extends GenericDAO<Project, Integer>
{
	/**
	 * Get all projects
	 * @return
	 */
	public List<Project> findAll();
	
	/**
	 * Get all active projects
	 * @return
	 */
	public List<Project> findAllActive();
	
	/**
	 * Get projects, filtered on name and active/inactive
	 * @param filter
	 * @param hideInactive
	 * @return
	 */
	public List<Project> findProjects(String filter, boolean hideInactive);
	
	/**
	 * Get all active default projects
	 * @return
	 */
	public List<Project> findDefaultProjects();
	
	/**
	 * Get projects for customer respecting the active flag
	 * @param customerIds
	 * @param active
	 * @return
	 */
	public List<Project> findProjectForCustomers(List<Integer> customerIds, boolean onlyActive);
	
	/**
	 * Find projects where user is projectmanager
	 * @param user
	 * @return
	 */
	public List<Project> findActiveProjectsWhereUserIsPM(User user);
}
