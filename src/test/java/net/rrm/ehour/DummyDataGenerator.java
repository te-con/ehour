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

import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.user.domain.UserDepartment;
import net.rrm.ehour.user.domain.UserRole;

/**
 * TODO 
 **/

public class DummyDataGenerator
{
	public static ProjectAssignment getProjectAssignment(int baseId)
	{
		ProjectAssignment	prjAsg;
		Project				prj;
		Customer			cust;
		User				user;
		
		cust = new Customer(baseId);
		cust.setActive(true);
		
		prj = new Project(baseId * 10);
		prj.setCustomer(cust);
		prj.setActive(true);
		
		prjAsg = new ProjectAssignment();
		prjAsg.setProject(prj);
		prjAsg.setAssignmentId(baseId * 100);
		
		user = new User(baseId * 1000);
		prjAsg.setUser(user);
		prjAsg.setActive(true);
		
		return prjAsg;
	}
	
	public static ProjectAssignmentAggregate getProjectAssignmentAggregate(int baseId)
	{
		ProjectAssignmentAggregate pag = new ProjectAssignmentAggregate();
		pag.setHours(baseId);
		pag.setTurnOver(baseId * 10);
		pag.setProjectAssignment(getProjectAssignment(baseId));
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
}
