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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.domain.Customer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Customer DAO test
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@SuppressWarnings("unchecked")
public class CustomerDAOTest  extends BaseDAOTest 
{
	@Autowired
	private	CustomerDAO	customerDAO;

	@Test
	public void testDelete()
	{
		Customer customer;

		customer = customerDAO.findById(new Integer(2));
		assertNotNull(customer);
		
		customerDAO.delete(customer);
		
		customer = customerDAO.findById(new Integer(2));
		assertNull(customer);
	}

	@Test
	public void testFindAll()
	{
		List customers = customerDAO.findAll();
		assertEquals(4, customers.size());
	}

	@Test
	public void testFindAllActive()
	{
		List customers = customerDAO.findAll(true);
		assertEquals(3, customers.size());
	}
	
	@Test
	public void testFindById()
	{
		Customer customer = customerDAO.findById(2);
		assertEquals("Tester", customer.getName());
	}

	@Test
	public void testPersist()
	{
		Customer	customer = new Customer();
		customer.setName("it's so real");
		customer.setDescription("what you feel");
		customer.setCode("oakenfold");
		customer.setActive(true);
		
		customerDAO.persist(customer);
		
		assertNotNull(customer.getCustomerId());
	}
	
	@Test
	public void testFindNameCode()
	{
		assertEquals("TEC", customerDAO.findOnNameAndCode("te-con", "TEC").getCode());
	}	

}
