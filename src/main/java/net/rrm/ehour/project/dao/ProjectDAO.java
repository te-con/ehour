package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.project.domain.Project;

/**
 * CRUD on project domain object
 * @author Thies
 *
 */

public interface ProjectDAO extends GenericDAO<Project, Integer>
{
	/**
	 * Get all projects (with active flag)
	 * @return
	 */
	public List<Project> findAll();
	
	/**
	 * Get all projects
	 * @param active
	 * @return
	 */
	public List<Project> findAll(boolean active);
	
	/**
	 * Get all active default projects
	 * @return
	 */
	public List<Project> findDefaultProjects();
}
