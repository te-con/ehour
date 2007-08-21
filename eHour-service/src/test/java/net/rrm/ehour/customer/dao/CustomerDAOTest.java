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
