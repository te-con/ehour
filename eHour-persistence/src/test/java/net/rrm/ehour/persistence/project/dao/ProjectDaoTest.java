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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;
import net.rrm.ehour.persistence.project.dao.ProjectDao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectDaoTest extends AbstractAnnotationDaoTest
{
	@Autowired
	private	ProjectDao	projectDAO;
	
	public ProjectDaoTest()
	{
		super("dataset-project.xml");
	}

	
	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.project.dao.ProjectDaoHibernateImpl#delete(net.rrm.ehour.persistence.persistence.domain.Project)}.
	 */
	@Test
	public void shouldDelete()
	{
		Project	project;
		
		project = projectDAO.findById(new Integer(2));
		assertNotNull(project);
		
		projectDAO.delete(project);
		
		project = projectDAO.findById(new Integer(2));
		assertNull(project);	
	}

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.project.dao.ProjectDaoHibernateImpl#findAll()}.
	 */
	@Test
	public void shouldFindAll()
	{
		List<Project> projects = projectDAO.findAll();
		
		assertEquals(5, projects.size());
	}

	@Test
	public void shouldFindAllFiltered()
	{
		List<Project> projects = projectDAO.findProjects("days", true);
		
		assertEquals(2, projects.size());
	}	

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.project.dao.ProjectDaoHibernateImpl#findById(java.lang.Integer)}.
	 */
	@Test
	public void shouldFindById()
	{
		Project prj = projectDAO.findById(1);
		assertEquals("eHour", prj.getName());
	}

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.project.dao.ProjectDaoHibernateImpl#persist(net.rrm.ehour.persistence.persistence.domain.Project)}.
	 */
	@Test
	public void shouldPersist()
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
	public void shouldFindAllActive()
	{
		List<Project> r = projectDAO.findAllActive();
		
		assertEquals(3, r.size());
	}
	
	@Test
	public void shouldFindDefaultProjects()
	{
		assertEquals(2, projectDAO.findDefaultProjects().size());
	}
	
	@Test
	public void shouldFindProjectForCustomersAll()
	{
		List<Customer> ids = new ArrayList<Customer>();
		ids.add(new Customer(3));
		ids.add(new Customer(1));
		List<Project> r = projectDAO.findProjectForCustomers(ids, false);
		
		assertEquals(3, r.size());
	}

	@Test
	public void shouldFindProjectForCustomersOnlyActive()
	{
		List<Customer> customers = new ArrayList<Customer>();
		customers.add(new Customer(3));
		customers.add(new Customer(1));
		List<Project> r = projectDAO.findProjectForCustomers(customers, true);
		
		assertEquals(2, r.size());
	}
	
	@Test
	public void shouldFindActiveProjectsWhereUserIsPM()
	{
		List<Project> res = projectDAO.findActiveProjectsWhereUserIsPM(new User(1));
		assertEquals(new Integer(1), res.iterator().next().getPK());
	}
}
