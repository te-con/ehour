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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * ProjectAssignmentDAO junit tests
 **/
@SuppressWarnings("deprecation")
public class ProjectAssignmentDaoTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	ProjectAssignmentDao	projectAssignmentDAO;
	
	public ProjectAssignmentDaoTest()
	{
		super("dataset-projectassignment.xml");
	}
	
	@Test
	public void shouldFindProjectAssignmentForProjectIdAndUserId()
	{
		List<ProjectAssignment> pas = projectAssignmentDAO.findProjectAssignmentForUser(1, 1);
		
		assertEquals(4, pas.size());
	}

	@Test
	public void shouldFindProjectAssignmentsForUser()
	{
		List<ProjectAssignment> pas = projectAssignmentDAO.findProjectAssignmentsForUser(new User(1));
		
		assertEquals(7, pas.size());
	}
	
	@Test
	public void shouldFindAll()
	{
		List<ProjectAssignment> pas = projectAssignmentDAO.findAll();
		
		assertEquals(12, pas.size());
	}

	@Test
	public void shouldDelete()
	{
		ProjectAssignment pa;
		
		pa = projectAssignmentDAO.findById(new Integer(2));
		assertNotNull(pa);
		
		projectAssignmentDAO.delete(pa);
		
		pa = projectAssignmentDAO.findById(new Integer(2));
		assertNull(pa);
	}

	@Test
	public void shouldPersist()
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
		projectAssignmentDAO.persist(pa);
	}

	@Test
	public void shouldFindById()
	{
		ProjectAssignment pa = projectAssignmentDAO.findById(1);
		
		assertEquals("eHour", pa.getProject().getName());
	}

	@Test
	public void shouldFindProjectAssignmentsForUserInRange()
	{
		List<ProjectAssignment> results;
		DateRange range = new DateRange(new Date(2006 - 1900, 10 - 1, 24), new Date(2007 - 1900, 1 - 1, 10));
		
		results = projectAssignmentDAO.findProjectAssignmentsForUser(1, range);

		assertEquals(5, results.size());
	}
	@Test
	public void shouldFindProjectAssignmentsForCustomer()
	{
		Customer cust = new Customer(3);
		List<ProjectAssignment> results;
		DateRange range = new DateRange(new Date(2006 - 1900, 8 - 1, 24), new Date(2007 - 1900, 1 - 1, 10));
		
		results = projectAssignmentDAO.findProjectAssignmentsForCustomer(cust, range);
		
		assertEquals(2, results.size());
	}
	
	public void shouldFindAllAssignmentsForProject()
	{
		Project prj = new Project(1);
		
		List<ProjectAssignment> list = projectAssignmentDAO.findProjectAssignments(prj);
		
		assertEquals(4, list.size());
	}

	public void shouldFindActiveAssignmentsForProject()
	{
		Project prj = new Project(1);
		
		List<ProjectAssignment> list = projectAssignmentDAO.findProjectAssignments(prj, Boolean.TRUE);
		
		assertEquals(3, list.size());
	}
}
