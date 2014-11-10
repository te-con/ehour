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

package net.rrm.ehour.ui.manage.customer;

import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.junit.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Customer admin test render
 */

public class CustomerManagePageTest extends BaseSpringWebAppTester {
    @Test
    public void testCustomerAdminRender() {
        CustomerService customerService = mock(CustomerService.class);
        getMockContext().putBean("customerService", customerService);

        when(customerService.getActiveCustomers()).thenReturn(new ArrayList<Customer>());

        getTester().startPage(CustomerManagePage.class);
        getTester().assertRenderedPage(CustomerManagePage.class);
        getTester().assertNoErrorMessage();
    }

}
