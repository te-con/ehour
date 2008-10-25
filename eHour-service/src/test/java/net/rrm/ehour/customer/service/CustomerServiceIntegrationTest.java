/**
 * Created on Aug 21, 2007
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.rrm.ehour.AbstractServiceTest;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
/**
 * Customer service test that goes to the database
 **/
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerServiceIntegrationTest extends AbstractServiceTest
{
	@Autowired
	private CustomerService	customerService;

	/**
	 * @throws ObjectNotFoundException 
	 * 
	 */
	@Test
	public void testGetCustomerDeletableTrue() throws ObjectNotFoundException
	{
		Customer customer = customerService.getCustomerAndCheckDeletability(4);
		assertTrue(customer.isDeletable());
	}

	/**
	 * @throws ObjectNotFoundException 
	 * 
	 */
	@Test
	public void testGetCustomerDeletableFalse() throws ObjectNotFoundException
	{
		Customer customer = customerService.getCustomerAndCheckDeletability(2);
		assertFalse(customer.isDeletable());
	}
}
