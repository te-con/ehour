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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectDAOTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	ProjectDAO	projectDAO;
	
	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#delete(net.rrm.ehour.domain.Project)}.
	 */
	@Test
	public void testDelete()
	{
		Project	project;
		
		project = projectDAO.findById(new Integer(2));
		assertNotNull(project);
		
		projectDAO.delete(project);
		
		project = projectDAO.findById(new Integer(2));
		assertNull(project);	
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#findAll()}.
	 */
	@Test
	public void testFindAll()
	{
		List projects = projectDAO.findAll();
		
		assertEquals(5, projects.size());
	}

	@Test
	public void testFindAllFiltered()
	{
		List projects = projectDAO.findProjects("days", true);
		
		assertEquals(2, projects.size());
	}	

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#findById(java.lang.Integer)}.
	 */
	@Test
	public void testFindById()
	{
		Project prj = projectDAO.findById(1);
		assertEquals("eHour", prj.getName());
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#persist(net.rrm.ehour.domain.Project)}.
	 */
	@Test
	public void testPersist()
	{
		Customer	customer = new Customer();
		customer.setCustomerId(1);

		
		Project	prj = new Project();
		prj.setName("test");
		prj.setCustomer(customer);
		prj.setDefaultProject(false);
		prj.setActive(true);
		prj.setProjectCode("bla");
		
		prj = projectDAO.persist(prj);
		
		assertNotNull(prj.getProjectId());
	}

	@Test
	public void testFindAllActive()
	{
		List r = projectDAO.findAllActive();
		
		assertEquals(3, r.size());
	}
	
	@Test
	public void testFindDefaultProjects()
	{
		assertEquals(2, projectDAO.findDefaultProjects().size());
	}
	
	@Test
	public void testFindProjectForCustomersAll()
	{
		List	ids = new ArrayList();
		ids.add(new Customer(3));
		ids.add(new Customer(1));
		List r = projectDAO.findProjectForCustomers(ids, false);
		
		assertEquals(3, r.size());
	}

	@Test
	public void testFindProjectForCustomersOnlyActive()
	{
		List	ids = new ArrayList();
		ids.add(new Customer(3));
		ids.add(new Customer(1));
		List r = projectDAO.findProjectForCustomers(ids, true);
		
		assertEquals(2, r.size());
	}
	
	@Test
	public void testFindActiveProjectsWhereUserIsPM()
	{
		List<Project> res = projectDAO.findActiveProjectsWhereUserIsPM(new User(1));
		assertEquals(new Integer(1), res.iterator().next().getPK());
	}
}
