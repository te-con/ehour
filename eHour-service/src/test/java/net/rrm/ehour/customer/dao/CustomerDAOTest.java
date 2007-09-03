/**
 * Created on Nov 25, 2006
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

package net.rrm.ehour.customer.dao;

import java.util.List;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.dao.BaseDAOTest;

/**
 * TODO 
 **/

public class CustomerDAOTest  extends BaseDAOTest 
{
	private	CustomerDAO	dao;

	
	public void setCustomerDAO(CustomerDAO dao)
	{
		this.dao = dao;
	}	
	
	public void testDelete()
	{
		Customer customer;

		customer = dao.findById(new Integer(2));
		assertNotNull(customer);
		
		dao.delete(customer);
		
		customer = dao.findById(new Integer(2));
		assertNull(customer);
	}

	public void testFindAll()
	{
		List customers = dao.findAll();
		assertEquals(4, customers.size());
	}

	public void testFindAllActive()
	{
		List customers = dao.findAll(true);
		assertEquals(3, customers.size());
	}
	
	public void testFindById()
	{
		Customer customer = dao.findById(2);
		assertEquals("Tester", customer.getName());
	}

	public void testPersist()
	{
		Customer	customer = new Customer();
		customer.setName("it's so real");
		customer.setDescription("what you feel");
		customer.setCode("oakenfold");
		customer.setActive(true);
		
		dao.persist(customer);
		
		assertNotNull(customer.getCustomerId());
	}

}
