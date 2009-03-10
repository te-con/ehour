/**
 * Created on Dec 4, 2006
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

package net.rrm.ehour.project.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * TODO 
 **/

@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectDAOTest extends BaseDAOTest
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
