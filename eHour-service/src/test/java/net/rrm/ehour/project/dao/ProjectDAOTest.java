/**
 * Created on Dec 4, 2006
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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.project.domain.Project;

/**
 * TODO 
 **/

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
		ids.add(3);
		ids.add(1);
		List r = dao.findProjectForCustomers(ids, false);
		
		assertEquals(3, r.size());
	}

	public void testFindProjectForCustomersOnlyActive()
	{
		List	ids = new ArrayList();
		ids.add(3);
		ids.add(1);
		List r = dao.findProjectForCustomers(ids, true);
		
		assertEquals(2, r.size());
	}
}
