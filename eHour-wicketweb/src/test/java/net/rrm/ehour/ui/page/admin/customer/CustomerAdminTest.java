/**
 * Created on Aug 20, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page.admin.customer;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.ui.common.BaseUITest;


/**
 * Customer admin test render
 **/

public class CustomerAdminTest extends BaseUITest
{
	/**
	 * Test render
	 */
	public void testCustomerAdminRender()
	{
		CustomerService customerService = createMock(CustomerService.class);
		mockContext.putBean("customerService", customerService);
		
		expect(customerService.getCustomers(true)).andReturn(new ArrayList<Customer>());

		replay(customerService);
		
		tester.startPage(CustomerAdmin.class);
		tester.assertRenderedPage(CustomerAdmin.class);
		tester.assertNoErrorMessage();
		
		verify(customerService);
	}
}
