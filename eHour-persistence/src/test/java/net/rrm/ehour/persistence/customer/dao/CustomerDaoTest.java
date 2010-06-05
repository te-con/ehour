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

package net.rrm.ehour.persistence.customer.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.persistence.dao.AbstractAnnotationDaoTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Customer DAO test
 **/

public class CustomerDaoTest extends AbstractAnnotationDaoTest 
{
	@Autowired
	private	CustomerDao	customerDao;

	public CustomerDaoTest()
	{
		super("dataset-customer.xml");
	}
	
	@Test
	public void shouldDeleteCustomerOnId()
	{
		Customer customer;

		customer = customerDao.findById(new Integer(2));
		assertNotNull(customer);
		
		customerDao.delete(customer);
		
		customer = customerDao.findById(new Integer(2));
		assertNull(customer);
	}

	@Test
	public void shouldFindAll()
	{
		List<Customer> customers = customerDao.findAll();
		assertEquals(4, customers.size());
	}

	@Test
	public void shouldFindAllActive()
	{
		List<Customer> customers = customerDao.findAllActive();
		assertEquals(3, customers.size());
	}
	
	@Test
	public void shouldFindOnId()
	{
		Customer customer = customerDao.findById(2);
		assertEquals("Tester", customer.getName());
	}

	@Test
	public void shouldPersist()
	{
		Customer customer = new Customer();
		customer.setName("it's so real");
		customer.setDescription("what you feel");
		customer.setCode("oakenfold");
		customer.setActive(true);
		
		customerDao.persist(customer);
		
		assertNotNull(customer.getCustomerId());
	}
	
	@Test
	public void shouldFindOnNameAndCode()
	{
		assertEquals("TEC", customerDao.findOnNameAndCode("te-con", "TEC").getCode());
	}	

}
