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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.dao.GenericDao;

import java.util.List;

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
	List<ProjectAssignment> findProjectAssignmentForUser(Integer projectId, Integer userId);

    /**
	 * Find (active) projects for user
	 * @param userId
	 * @return
	 */
	List<ProjectAssignment> findProjectAssignmentsForUser(User user);
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range);
	
	/**
	 * Find project assignments for project in range
	 * @param project
	 * @param range
	 * @return
	 */
	List<ProjectAssignment> findProjectAssignmentsForProject(Project project, DateRange range);
	
	/**
	 * Find project assignments for customer in date range
	 * @param project
	 * @param range
	 * @return
	 */
	List<ProjectAssignment> findProjectAssignmentsForCustomer(Customer customer, DateRange range);
	
	/**
	 * Find all project assignment types (should be in a seperate DAO theoratically)
	 * @return
	 */
	List<ProjectAssignmentType>	findProjectAssignmentTypes();
	
	/**
	 * 
	 * @param project
	 * @param onlyActive
	 * @return
	 */
	List<ProjectAssignment> findProjectAssignments(Project project);
	
	List<ProjectAssignment> findProjectAssignments(Project project, Boolean onlyActive);
}
