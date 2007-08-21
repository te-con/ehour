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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.EhourConstants;

/**
 * TODO 
 **/

public class ProjectAssignmentDAOTest extends BaseDAOTest
{
	private	ProjectAssignmentDAO	dao;
	
	public void setProjectAssignmentDAO(ProjectAssignmentDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectAssignmentDAOHibernateImpl#findProjectsForUser(java.lang.Integer, java.lang.Integer)}.
	 */
	public void testFindProjectAssignmentForUser()
	{
		List<ProjectAssignment> pas = dao.findProjectAssignmentForUser(1, 1);
		
		assertEquals(4, pas.size());
	}

	/**
	 * 
	 *
	 */
	public void testFindProjectAssignmentsForUser()
	{
		List<ProjectAssignment> pas = dao.findProjectAssignmentsForUser(1);
		
		assertEquals(7, pas.size());
	}
	
	
	/**
	 * Test method for {@link net.rrm.ehour.dao.GenericDAOHibernateImpl#findAll()}.
	 */
	public void testFindAll()
	{
		List<ProjectAssignment> pas = dao.findAll();
		
		assertEquals(10, pas.size());
	}

	/**
	 * Test method for {@link net.rrm.ehour.dao.GenericDAOHibernateImpl#delete(net.rrm.ehour.domain.DomainObject)}.
	 */
	public void testDelete()
	{
		ProjectAssignment pa;
		
		pa = dao.findById(new Integer(2));
		assertNotNull(pa);
		
		dao.delete(pa);
		
		pa = dao.findById(new Integer(2));
		assertNull(pa);
	}

	/**
	 * Test method for {@link net.rrm.ehour.dao.GenericDAOHibernateImpl#persist(net.rrm.ehour.domain.DomainObject)}.
	 */
	public void testPersist()
	{
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.MONTH, 1);
		ProjectAssignment pa = new ProjectAssignment();
		User	user = new User(2);
		Project prj = new Project(1);
		
		pa.setProject(prj);
		pa.setUser(user);
		pa.setDateStart(new Date());
		pa.setDateEnd(cal.getTime());
		pa.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		dao.persist(pa);
	}

	/**
	 * Test method for {@link net.rrm.ehour.dao.GenericDAOHibernateImpl#findById(java.io.Serializable)}.
	 */
	public void testFindById()
	{
		ProjectAssignment pa = dao.findById(1);
		
		assertEquals("eHour", pa.getProject().getName());
	}

	public void testFindProjectAssignmentsForUserInRange()
	{
		List<ProjectAssignment> results;
		DateRange range = new DateRange(new Date(2006 - 1900, 10 - 1, 24), new Date(2007 - 1900, 1 - 1, 10));
		
		results = dao.findProjectAssignmentsForUser(1, range);

		assertEquals(5, results.size());
	}
}
