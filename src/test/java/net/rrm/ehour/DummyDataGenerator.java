/**
 * Created on 7-feb-2007
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

package net.rrm.ehour;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;

/**
 * DummyDataGenerator 
 **/

public class DummyDataGenerator
{
	/**
	 * 
	 * 
	 * @param baseIds baseId[0] = baseId, baseId[1] = customerId, baseId[2] = userId
	 * 			baseIds[3] = projectId, baseId[4] = assignmentId
	 * @return
	 */
	public static ProjectAssignment getProjectAssignment(int... baseIds)
	{
		ProjectAssignment	prjAsg;
		Project				prj;
		Customer			cust;
		User				user;
		int					customerId, userId, projectId, assignmentId;
		
		// IMHO varargs is a stupid addition to the Java language
		// ah yes, it cleans up your API but it adds an MSN signature style and a lot of bogus code to parse that MSN style crap
		// let alone that it's completely unknown at what the exact signature functionally means
		// oh we explain that in javadoc? right..
		// you know what, why don't we drop type safety at all and change every argument to Object ?
		int baseId = baseIds[0];
		
		customerId = baseId;
		userId = baseId;
		projectId = baseId * 10;
		assignmentId = baseId * 100;
		
		if (baseIds.length >= 2)
		{
			customerId = baseIds[1];
			userId = customerId;
		}

		if (baseIds.length >= 3)
		{
			userId = baseIds[2];
		}
		
		if (baseIds.length >= 4)
		{
			projectId = baseIds[3];
		}		

		if (baseIds.length >= 5)
		{
			assignmentId = baseIds[4];
		}		

		
		cust = getCustomer(customerId);
		
		prj = new Project(projectId);
		prj.setCustomer(cust);
		prj.setActive(true);
		prj.setName("tralala" + baseId);
		
		prjAsg = new ProjectAssignment();
		prjAsg.setProject(prj);
		prjAsg.setAssignmentId(assignmentId);
		
		user = getUser();
		user.setUserId(userId);
		
		prjAsg.setAssignmentType(new ProjectAssignmentType(ProjectAssignmentUtil.TYPE_START_END_DATE));

		prjAsg.setUser(user);
		prjAsg.setActive(true);
		
		return prjAsg;
	}
	
	public static Customer getCustomer(int customerId)
	{
		Customer cust = new Customer(customerId);
		cust.setName(customerId + "");
		cust.setActive(true);
		
		return cust;
	}

	public static ProjectAssignmentAggregate getProjectAssignmentAggregate(int baseId, int customerId, int userId)
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(baseId);
		pag.setTurnOver(baseId * 10);
		pag.setProjectAssignment(getProjectAssignment(baseId, customerId, userId));
		return pag;
	}
	
	public static User getUser()
	{
		User user = new User();
		
		user.setActive(true);
		user.setEmail("thies@te-con.nl");
		user.setUsername("testmetoo");
		user.setFirstName("Dummy");
		user.setLastName("TestUser");
		user.setPassword("abc");
		
		user.setUserDepartment(new UserDepartment(1));
		
		Set<UserRole> roles = new HashSet<UserRole>();
		roles.add(new UserRole("ROLE_ADMIN"));
		user.setUserRoles(roles);
		
		return user;
	}
	
	public static TimesheetEntry getTimesheetEntry(int prjId, Date date, float hours)
	{
		TimesheetEntry 	entry;
		TimesheetEntryId id;
		
		id = new TimesheetEntryId();
		id.setEntryDate(date);
		id.setProjectAssignment(getProjectAssignment(prjId, prjId, 1));
		
		entry = new TimesheetEntry();
		entry.setEntryId(id);
		entry.setHours(hours);
		
		return entry;
	}
}
