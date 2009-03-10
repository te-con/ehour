/**
 * Created on Dec 11, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ProjectAlreadyAssignedException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectAssignmentServiceIntegrationTest extends AbstractServiceTest
{
	@Autowired
	private	ProjectAssignmentService projectAssignmentService;

	@Test
	public void testGetAllProjectsForUser()
	{
		List<ProjectAssignment> pas = projectAssignmentService.getProjectAssignmentsForUser(new User(1), false);
		assertEquals(7, pas.size());
	}

	@Test
	public void testGetAllProjectsForUserInactiveFiltered()
	{
		List<ProjectAssignment> pas = projectAssignmentService.getProjectAssignmentsForUser(new User(1), true);
		assertEquals(3, pas.size());
	}

	@Test	
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
	
	@Test	
	public void testGetProjectAssignmentsForUser()
	{
		DateRange dateRange = new DateRange();
		
		dateRange.setDateStart(new Date(2007 - 1900, 0, 1));
		dateRange.setDateEnd(new Date(2008 - 1900, 0, 1));
		
		List<ProjectAssignment> l = projectAssignmentService.getProjectAssignmentsForUser(1, dateRange);
		
		assertEquals(5, l.size());
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
}
