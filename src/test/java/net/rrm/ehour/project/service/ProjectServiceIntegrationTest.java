/**
 * Created on Dec 11, 2006
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

package net.rrm.ehour.project.service;

import java.util.GregorianCalendar;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.user.domain.User;

/**
 * TODO 
 **/

public class ProjectServiceIntegrationTest extends BaseDAOTest
{
	private	ProjectAssignmentService projectAssignmentService;

	
	public void setProjectAssignmentService(ProjectAssignmentService service)
	{
		this.projectAssignmentService=service;
	}
	
	public void testAssignUserToProjectSuccess() throws ProjectAlreadyAssignedException
	{
		ProjectAssignment pa = new ProjectAssignment();
		
		pa.setAssignmentId(1);
		pa.setAssignmentType(new ProjectAssignmentType(0));
		Project prj = new Project(1);
		User user = new User(1);
		
		pa.setDateStart(new GregorianCalendar(2006, 1, 1).getTime());
		pa.setDateEnd(new GregorianCalendar(2006, 2, 1).getTime());
		pa.setProject(prj);
		pa.setUser(user);
		
		projectAssignmentService.assignUserToProject(pa);
	}

//	public void testAssignUserToProjectFailure()
//	{
//		ProjectAssignment pa = new ProjectAssignment();
//		
//		Project prj = new Project(2);
//		User user = new User(1);
//		
//		pa.setDateStart(new GregorianCalendar(2006, 1, 1).getTime());
//		pa.setDateEnd(new GregorianCalendar(2006, 11, 1).getTime());
//		pa.setProject(prj);
//		pa.setUser(user);
//		pa.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
//		
//		try
//		{
//			projectAssignmentService.assignUserToProject(pa);
//			fail("Should throw ex");
//		} catch (ProjectAlreadyAssignedException e)
//		{
//		}
//	}

	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:/applicationContext-datasource.xml",
							  "classpath:/applicationContext-dao.xml",
							  "classpath:/applicationContext-mail.xml", 
							  "classpath:/applicationContext-service.xml"};	
	}
}
