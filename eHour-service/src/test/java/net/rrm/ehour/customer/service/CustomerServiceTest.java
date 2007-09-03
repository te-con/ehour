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

package net.rrm.ehour.customer.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.domain.Project;

/**
 * TODO 
 **/

public class CustomerServiceTest extends TestCase
{
	private	CustomerService	customerService;
	private	CustomerDAO		customerDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		customerService = new CustomerServiceImpl();

		customerDAO = createMock(CustomerDAO.class);
		((CustomerServiceImpl)customerService).setCustomerDAO(customerDAO);
	}


	public void testDeleteCustomerWithConstraintViolation()
	{
		Customer cust = new Customer();
		Project	proj = new Project();
		Set<Project>	projs = new HashSet<Project>();
		proj.setProjectId(1);
		cust.setCustomerId(1);
		
		projs.add(proj);
		cust.setProjects(projs);
		
		customerDAO.findById(1);
		expectLastCall().andReturn(cust);
		
		replay(customerDAO);

		try
		{
			customerService.deleteCustomer(1);
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
		
		customerService.deleteCustomer(1);
		
		verify(customerDAO);
	}
	

	public void testGetCustomer()
	{
		customerDAO.findById(1);
		expectLastCall().andReturn(null);
		
		replay(customerDAO);
		
		customerService.getCustomer(1);
		
		verify(customerDAO);
	}

	public void testGetCustomers()
	{
		customerDAO.findAll();
		expectLastCall().andReturn(null);
		
		replay(customerDAO);
		
		customerService.getCustomers();
		
		verify(customerDAO);
	}

	public void testGetActiveCustomers()
	{
		customerDAO.findAll(true);
		expectLastCall().andReturn(null);
		
		replay(customerDAO);
		
		customerService.getCustomers(true);
		
		verify(customerDAO);
	}	
	
	public void testPersistCustomer() throws ObjectNotUniqueException
	{
		Customer cust = new Customer();
		cust.setCustomerId(1);
		
		customerDAO.persist(cust);
		expectLastCall().andReturn(cust);
		
		replay(customerDAO);
		
		customerService.persistCustomer(cust);
		
		verify(customerDAO);
	}

}
