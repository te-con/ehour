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

package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.AbstractGenericDaoHibernateImpl;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * CRUD stuff on PA do 
 **/
@Repository("projectAssignmentDAO")
public class ProjectAssignmentDAOHibernateImpl
	extends AbstractGenericDaoHibernateImpl<ProjectAssignment, Integer> 
	implements ProjectAssignmentDAO
{
	protected final static String	CACHEREGION = "query.ProjectAssignment";
	
	/**
	 * @todo fix this a bit better
	 */
	public ProjectAssignmentDAOHibernateImpl()
	{
		super(ProjectAssignment.class);
	}
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range)
	{
		List<ProjectAssignment>		results;

		String[]	keys = new String[]{"dateStart", "dateEnd", "userId"};
		Object[]	params = new Object[]{range.getDateStart(), range.getDateEnd(), userId}; 
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserInRange"
				, keys
				, params
				, true
				, CACHEREGION);			
		
		
		return results;
	}
	
	
	/**
	 * Find assigned (active) projects for user
	 * @param projectId
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId)
	{
		String[]	keys = new String[]{"projectId", "userId"};
		Integer[]	params = new Integer[]{projectId, userId};
		List<ProjectAssignment>		results;
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUserForProject"
				, keys
				, params
				, true
				, CACHEREGION);		
		
		return results;
	}
	
	/**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */	
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForUser(User user)
	{
		List<ProjectAssignment>		results;
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForUser"
				, "user"
				, user
				, true
				, CACHEREGION);			
		
		return results;
	}

	/**
	 * Find project assignments for user for a specific type
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId, Integer assignmentType)
	{
		List<ProjectAssignment>		results;
		String[]	names = new String[]{"userId", "type"};
		Integer[]	values = new Integer[]{userId, assignmentType};
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectsForUserForType"
				, names
				, values
				, true
				, CACHEREGION);		
		
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectAssignmentDAO#findProjectAssignmentsForProject(net.rrm.ehour.project.domain.Project, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range)
	{
		List<ProjectAssignment>		results;
		String[]	keys = new String[]{"dateStart", "dateEnd", "project"};
		Object[]	params = new Object[]{range.getDateStart(), range.getDateEnd(), project}; 
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForProjectInRange"
													, keys
													, params
													, true
													, CACHEREGION);
		
		return results;
	}

	public List<ProjectAssignment> findProjectAssignments(Project project)
	{
		return findProjectAssignments(project, null);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignments(Project project, Boolean onlyActive)
	{
		Criteria crit = getSession().createCriteria(ProjectAssignment.class);
		
		if (onlyActive != null)
		{
			crit.add(Restrictions.eq("active", onlyActive));
		}
		
		crit.add(Restrictions.eq("project", project));
		
		return crit.list();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectAssignmentDAO#findProjectAssignmentTypes()
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignmentType> findProjectAssignmentTypes()
	{
		return getHibernateTemplate().loadAll(ProjectAssignmentType.class);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.dao.ProjectAssignmentDAO#findProjectAssignmentsForCustomer(net.rrm.ehour.customer.domain.Customer, net.rrm.ehour.data.DateRange)
	 */
	@SuppressWarnings("unchecked")
	public List<ProjectAssignment> findProjectAssignmentsForCustomer(Customer customer, DateRange range)
	{
		List<ProjectAssignment>		results;
		String[]	keys = new String[]{"dateStart", "dateEnd", "customer"};
		Object[]	params = new Object[]{range.getDateStart(), range.getDateEnd(), customer}; 
		
		results = findByNamedQueryAndNamedParam("ProjectAssignment.findProjectAssignmentsForCustomerInRange"
													, keys
													, params
													, true
													, CACHEREGION);
		
		return results;	}
}
