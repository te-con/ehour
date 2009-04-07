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
