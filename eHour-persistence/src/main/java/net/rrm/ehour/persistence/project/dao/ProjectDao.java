/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.persistence.project.dao;

import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.GenericDao;

/**
 * CRUD on project domain object
 * @author Thies
 *
 */

public interface ProjectDao extends GenericDao<Project, Integer>
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
	public List<Project> findProjectForCustomers(List<Customer> customers, boolean onlyActive);
	
	/**
	 * Find projects where user is projectmanager
	 * @param user
	 * @return
	 */
	public List<Project> findActiveProjectsWhereUserIsPM(User user);
}
