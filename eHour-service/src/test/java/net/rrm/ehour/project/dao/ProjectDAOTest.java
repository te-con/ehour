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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.project.domain.Project;

/**
 * TODO 
 **/

@SuppressWarnings("unchecked")
public class ProjectDAOTest extends BaseDAOTest
{
	private	ProjectDAO	dao;
	
	public void setProjectDAO(ProjectDAO dao)
	{
		this.dao = dao;
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#delete(net.rrm.ehour.project.domain.Project)}.
	 */
	public void testDelete()
	{
		Project	project;
		
		project = dao.findById(new Integer(2));
		assertNotNull(project);
		
		dao.delete(project);
		
		project = dao.findById(new Integer(2));
		assertNull(project);	
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#findAll()}.
	 */
	public void testFindAll()
	{
		List projects = dao.findAll();
		
		assertEquals(5, projects.size());
	}

	public void testFindAllFiltered()
	{
		List projects = dao.findProjects("days", true);
		
		assertEquals(2, projects.size());
	}	

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#findById(java.lang.Integer)}.
	 */
	public void testFindById()
	{
		Project prj = dao.findById(1);
		assertEquals("eHour", prj.getName());
	}

	/**
	 * Test method for {@link net.rrm.ehour.project.dao.ProjectDAOHibernateImpl#persist(net.rrm.ehour.project.domain.Project)}.
	 */
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
		
		prj = dao.persist(prj);
		
		assertNotNull(prj.getProjectId());
	}

	public void testFindAllActive()
	{
		List r = dao.findAllActive();
		
		assertEquals(3, r.size());
	}
	
	public void testFindDefaultProjects()
	{
		assertEquals(2, dao.findDefaultProjects().size());
	}
	
	public void testFindProjectForCustomersAll()
	{
		List	ids = new ArrayList();
		ids.add(new Customer(3));
		ids.add(new Customer(1));
		List r = dao.findProjectForCustomers(ids, false);
		
		assertEquals(3, r.size());
	}

	public void testFindProjectForCustomersOnlyActive()
	{
		List	ids = new ArrayList();
		ids.add(new Customer(3));
		ids.add(new Customer(1));
		List r = dao.findProjectForCustomers(ids, true);
		
		assertEquals(2, r.size());
	}
}
