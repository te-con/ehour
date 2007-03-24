/**
 * Created on Dec 10, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;

/**
 * CRUD on ProjectAssignment domain object
 **/

public interface ProjectAssignmentDAO  extends GenericDAO<ProjectAssignment, Integer>
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
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId);
	
	/**
	 * Find (active) projects for user in date range
	 * @param userId
	 * @param range
	 * @return
	 */
	public List<ProjectAssignment> findProjectAssignmentsForUser(Integer userId, DateRange range);
}
