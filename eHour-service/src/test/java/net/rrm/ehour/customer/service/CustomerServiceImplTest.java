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

package net.rrm.ehour.customer.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerObjectMother;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceImplTest {
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerDao customerDAO;
    private Customer customer;

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl();
        customerService.setCustomerDAO(customerDAO);

        customer = CustomerObjectMother.createCustomer(1);

    }

    @Test
    public void should_delete_customer_with_projects_throw_a_constraint_violation() {
        Customer cust = new Customer();
        Project proj = new Project();
        Set<Project> projs = new HashSet<>();
        proj.setProjectId(1);
        cust.setCustomerId(1);

        projs.add(proj);
        cust.setProjects(projs);

        when(customerDAO.findById(1)).thenReturn(cust);

        try {
            customerService.deleteCustomer(1);
            fail("no ParentChildConstraintException thrown");
        } catch (ParentChildConstraintException e) {
        }
    }

    @Test
    public void should_delete_customer() throws ParentChildConstraintException {
        int customerId = 2;
        Customer customer2 = new Customer(customerId);
        when(customerDAO.findById(customerId)).thenReturn(customer2);

        customerService.deleteCustomer(customerId);

        verify(customerDAO).delete(customer2);
    }

    @Test
    public void should_get_customer() throws ObjectNotFoundException {
        when(customerDAO.findById(1)).thenReturn(customer);

        assertEquals(customer, customerService.getCustomer(1));
    }

    @Test
    public void should_get_all_customers() {
        when(customerDAO.findAll()).thenReturn(Lists.newArrayList(customer));

        assertEquals(1, customerService.getCustomers().size());
    }

    @Test
    public void should_get_only_active_customers() {
        when(customerDAO.findAllActive()).thenReturn(Lists.newArrayList(customer));
        assertEquals(1, customerService.getActiveCustomers().size());
    }

    @Test
    public void should_persist_customer() throws ObjectNotUniqueException {
        customerService.persistCustomer(customer);

        verify(customerDAO).persist(customer);
    }
}
