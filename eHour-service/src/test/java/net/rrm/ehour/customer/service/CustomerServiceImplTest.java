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

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.fail;

public class CustomerServiceImplTest {
    private CustomerServiceImpl customerService;

    private UserService userService;

    private CustomerDao customerDAO;

    @Before
    public void setUp() {
        customerService = new CustomerServiceImpl();
        userService = createMock(UserService.class);
        customerDAO = createMock(CustomerDao.class);
        customerService.setCustomerDAO(customerDAO);
        customerService.setUserService(userService);
    }

    @Test
    public void testDeleteCustomerWithConstraintViolation() {
        Customer cust = new Customer();
        Project proj = new Project();
        Set<Project> projs = new HashSet<Project>();
        proj.setProjectId(1);
        cust.setCustomerId(1);

        projs.add(proj);
        cust.setProjects(projs);

        customerDAO.findById(1);
        expectLastCall().andReturn(cust);

        replay(customerDAO);

        try {
            customerService.deleteCustomer(1);
            fail("no ParentChildConstraintException thrown");

        } catch (ParentChildConstraintException ignored) {
        }

        verify(customerDAO);
    }

    @Test
    public void testDeleteCustomer() throws ParentChildConstraintException {
        Customer cust = new Customer();
        cust.setCustomerId(1);

        customerDAO.findById(1);
        expectLastCall().andReturn(cust);

        customerDAO.delete(cust);

        replay(customerDAO);

        customerService.deleteCustomer(1);

        verify(customerDAO);
    }


    @Test
    public void testGetCustomer() throws ObjectNotFoundException {
        customerDAO.findById(1);
        expectLastCall().andReturn(new Customer(1));

        replay(customerDAO);

        customerService.getCustomer(1);

        verify(customerDAO);
    }

    @Test
    public void testGetCustomers() {
        customerDAO.findAll();
        expectLastCall().andReturn(null);

        replay(customerDAO);

        customerService.getCustomers();

        verify(customerDAO);
    }

    @Test
    public void testGetActiveCustomers() {
        customerDAO.findAllActive();
        expectLastCall().andReturn(null);

        replay(customerDAO);

        customerService.getActiveCustomers();

        verify(customerDAO);
    }

    @Test
    public void testPersistCustomer() throws ObjectNotUniqueException {
        Customer cust = new Customer();
        cust.setCustomerId(1);

        customerDAO.persist(cust);
        expectLastCall().andReturn(cust);

        replay(customerDAO);

        customerService.persistCustomer(cust);

        verify(customerDAO);
    }

    @Test
    public void testPersistCustomerContainingReviewers() throws ObjectNotUniqueException {
        Customer cust = new Customer();
        cust.setCustomerId(1);

        List<User> reviewers = new ArrayList<User>();
        reviewers.add(new User(1, "thies"));
        reviewers.add(new User(2, "morgan"));
        cust.setReviewers(reviewers);

        expect(customerDAO.persist(cust)).andReturn(cust);

        expect(userService.addRole(1, UserRole.CUSTOMERREVIEWER)).andReturn(new User(1, "thies"));
        expect(userService.addRole(2, UserRole.CUSTOMERREVIEWER)).andReturn(new User(1, "thies"));

        replay(customerDAO, userService);

        customerService.persistCustomer(cust);

        verify(customerDAO, userService);
    }

    @Test
    public void testPersistCustomerContainingReporters() throws ObjectNotUniqueException
    {
        Customer cust = new Customer();
        cust.setCustomerId(1);

        List<User> reporters = new ArrayList<User>();
        reporters.add(new User(1, "thies"));
        reporters.add(new User(2, "morgan"));
        cust.setReporters(reporters);

        expect(customerDAO.persist(cust)).andReturn(cust);

        expect(userService.addRole(1, UserRole.CUSTOMERREPORTER)).andReturn(new User(1, "thies"));
        expect(userService.addRole(2, UserRole.CUSTOMERREPORTER)).andReturn(new User(1, "thies"));

        replay(customerDAO, userService);

        customerService.persistCustomer(cust);

        verify(customerDAO, userService);
    }

    @Test
    public void testPersistCustomerContainingBothReportersAndReviewers() throws ObjectNotUniqueException
    {
        Customer cust = new Customer();
        cust.setCustomerId(1);

        List<User> reporters = new ArrayList<User>();
        reporters.add(new User(1, "thies"));
        reporters.add(new User(2, "morgan"));
        cust.setReporters(reporters);

        List<User> reviewers = new ArrayList<User>();
        reviewers.add(new User(3, "laurent"));
        cust.setReviewers(reviewers);

        expect(customerDAO.persist(cust)).andReturn(cust);

        expect(userService.addRole(1, UserRole.CUSTOMERREPORTER)).andReturn(new User(1, "thies"));
        expect(userService.addRole(2, UserRole.CUSTOMERREPORTER)).andReturn(new User(1, "thies"));
        expect(userService.addRole(3, UserRole.CUSTOMERREVIEWER)).andReturn(new User(3, "laurent"));

        replay(customerDAO, userService);

        customerService.persistCustomer(cust);

        verify(customerDAO, userService);
    }

}
