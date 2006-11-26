/**
 * Created on Nov 25, 2006
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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.CustomerDAO;
import net.rrm.ehour.project.domain.Customer;
import net.rrm.ehour.project.domain.Project;

/**
 *  
 **/

public class ProjectServiceTest extends TestCase
{
	private	ProjectService	projectService;
	private	CustomerDAO		customerDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		projectService = new ProjectServiceImpl();

		customerDAO = createMock(CustomerDAO.class);
		((ProjectServiceImpl)projectService).setCustomerDAO(customerDAO);
	}


	public void testDeleteCustomerWithConstraintViolation()
	{
		Customer cust = new Customer();
		Project	proj = new Project();
		Set	projs = new HashSet();
		proj.setProjectId(1);
		cust.setCustomerId(1);
		
		projs.add(proj);
		cust.setProjects(projs);
		
		customerDAO.findById(1);
		expectLastCall().andReturn(cust);
		
		replay(customerDAO);

		try
		{
			projectService.deleteCustomer(1);
			fail("no ParentChildConstraintException thrown");
			
		} catch (ParentChildConstraintException e)
		{
		}
		
		verify(customerDAO);
	}
	
	public void testDeleteCustomer() throws ParentChildConstraintException
	{
		Customer cust = new Customer();
		cust.setCustomerId(1);
		
		customerDAO.findById(1);
		expectLastCall().andReturn(cust);
		
		customerDAO.delete(cust);
		
		replay(customerDAO);
		
		projectService.deleteCustomer(1);
		
		verify(customerDAO);
	}
	

	public void testGetCustomer()
	{
		customerDAO.findById(1);
		expectLastCall().andReturn(null);
		
		replay(customerDAO);
		
		projectService.getCustomer(1);
		
		verify(customerDAO);
	}

	public void testGetCustomers()
	{
		customerDAO.findAll();
		expectLastCall().andReturn(null);
		
		replay(customerDAO);
		
		projectService.getCustomers();
		
		verify(customerDAO);
	}

	public void testPersistCustomer()
	{
		Customer cust = new Customer();
		cust.setCustomerId(1);
		
		customerDAO.persist(cust);
		expectLastCall().andReturn(cust);
		
		replay(customerDAO);
		
		projectService.persistCustomer(cust);
		
		verify(customerDAO);
	}

}
