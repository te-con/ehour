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

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.dao.BaseDAOTest;

/**
 * Customer service test that goes to the database
 **/

public class CustomerServiceIntegrationTest extends BaseDAOTest
{
	private CustomerService	customerService;

	/**
	 * 
	 */
	public void testGetCustomerDeletableTrue()
	{
		Customer customer = customerService.getCustomerAndCheckDeletability(4);
		assertTrue(customer.isDeletable());
	}

	/**
	 * 
	 */
	public void testGetCustomerDeletableFalse()
	{
		Customer customer = customerService.getCustomerAndCheckDeletability(2);
		assertFalse(customer.isDeletable());
	}

	/**
	 * @param customerService the customerService to set
	 */
	public void setCustomerService(CustomerService customerService)
	{
		this.customerService = customerService;
	}
	
	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:/applicationContext-datasource.xml",
							  "classpath:/applicationContext-dao.xml",
							  "classpath:/applicationContext-mail.xml", 
							  "classpath:/applicationContext-service.xml"};	
	}		
}
