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

package net.rrm.ehour.ui.admin.customer.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.Test;


/**
 * Customer admin test render
 **/

public class CustomerAdminTest extends AbstractSpringWebAppTester
{
	/**
	 * Test render
	 */
	@Test
	public void testCustomerAdminRender()
	{
		CustomerService customerService = createMock(CustomerService.class);
		getMockContext().putBean("customerService", customerService);
		
		expect(customerService.getCustomers(true)).andReturn(new ArrayList<Customer>());

		replay(customerService);
		
		getTester().startPage(CustomerAdmin.class);
		getTester().assertRenderedPage(CustomerAdmin.class);
		getTester().assertNoErrorMessage();
		
		verify(customerService);
	}
	
//	/**
//	 * Test render
//	 */
//	public void testFormSubmit()
//	{
//		CustomerService customerService = createMock(CustomerService.class);
//		getMockContext().putBean("customerService", customerService);
//		
//		expect(customerService.getCustomers(true)).andReturn(new ArrayList<Customer>());
//
//		replay(customerService);
//		
//		FormTester form = getTester().newFormTester("tabs.customerForm");
//		form.submit();
//		
//		getTester().startPage(CustomerAdmin.class);
//		getTester().assertRenderedPage(CustomerAdmin.class);
//		getTester().assertNoErrorMessage();
//		
//		verify(customerService);
//	}	
}
