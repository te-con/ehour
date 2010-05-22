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

import net.rrm.ehour.dao.GenericDao;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;

/**
 * CRUD on ProjectAssignment domain object
 **/

public interface ProjectAssignmentDao  extends GenericDao<ProjectAssignment, Integer>
{
	/**
	 * Find assigned (active) project for user
	 * @param projectId
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId);
	
	/**
	 * Find assigned (active) projects for user with specific assignment type
	 * @param projectId
	 * @param userId
	 * @param assignmentType
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId, Integer assignmentType);
	
	/**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForUser(User user);
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range);
	
	/**
	 * Find project assignments for project in range
	 * @param project
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range);
	
	/**
	 * Find project assignments for customer in date range
	 * @param project
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForCustomer(Customer customer, DateRange range);
	
	/**
	 * Find all project assignment types (should be in a seperate DAO theoratically)
	 * @return
	 */
	public List<ProjectAssignmentType>	findProjectAssignmentTypes();
	
	/**
	 * 
	 * @param project
	 * @param onlyActive
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignments(Project project);
	
	public List<ProjectAssignment> findProjectAssignments(Project project, Boolean onlyActive);
}
