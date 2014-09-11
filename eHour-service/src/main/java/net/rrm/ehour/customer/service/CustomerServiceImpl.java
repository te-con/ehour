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

import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
import net.rrm.ehour.user.service.UserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Customer service implementation
 */
@Service("customerService")
public class CustomerServiceImpl implements CustomerService
{
	@Autowired
	private	CustomerDao		customerDAO;
	
	@Autowired
	private UserService     userService;
	
	private	static final Logger	LOGGER = Logger.getLogger(CustomerServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.customer.service.CustomerService#getCustomer(java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly=true)
	public Customer getCustomer(String customerName, String customerCode)
	{
		return customerDAO.findOnNameAndCode(customerName, customerCode);
	}

    @Override
    public List<Customer> findAllCustomersForWhichUserIsaReviewer(User user) {
        return customerDAO.findAllCustomersForWhichUserIsaReviewer(user);
    }

    @Override
    public Customer getCustomer(String customerCode) {
        return customerDAO.findByCustomerCode(customerCode);
    }

    @Auditable(actionType = AuditActionType.DELETE)
    @Transactional
    public void deleteCustomer(Integer customerId) throws ParentChildConstraintException {
        Customer customer = customerDAO.findById(customerId);

        LOGGER.info("Deleting customer: " + customer);

        if (customer != null) {
            if (customer.getProjects() != null &&
                    customer.getProjects().size() > 0) {
                throw new ParentChildConstraintException(customer.getProjects().size() + " projects attached to customer");
            } else {
                try {
                    customerDAO.delete(customer);
                } catch (DataIntegrityViolationException cve) {
                    throw new ParentChildConstraintException(cve);
                }
            }
        }
    }
    
    


    @Transactional(readOnly = true)
    public Customer getCustomer(Integer customerId) {
        return customerDAO.findById(customerId);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerAndCheckDeletability(Integer customerId) {
        Customer customer = customerDAO.findById(customerId);

        boolean canDelete = !(customer.getProjects() != null && customer.getProjects().size() > 0);
        customer.setDeletable(canDelete);

        return customer;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getCustomers() {
        return customerDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> getActiveCustomers() {
        return customerDAO.findAllActive();
    }

    @Transactional
    public Customer persistCustomer(Customer customer) throws ObjectNotUniqueException {
        LOGGER.info("Persisting customer: " + customer);

        try {
            customerDAO.persist(customer);

            addRoleToUsers(customer.getReviewers(), UserRole.CUSTOMERREVIEWER);

            addRoleToUsers(customer.getReporters(), UserRole.CUSTOMERREPORTER);
        } catch (DataIntegrityViolationException cve) {
            throw new ObjectNotUniqueException(cve);
        }

        return customer;
    }

    private void addRoleToUsers(List<User> users, UserRole userRole) {
        if (users != null && users.size() > 0) {
            for (User reviewer : users) {
                userService.addRole(reviewer.getUserId(), userRole);
            }
        }
    }

    public void setCustomerDAO(CustomerDao customerDAO) {
        this.customerDAO = customerDAO;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
