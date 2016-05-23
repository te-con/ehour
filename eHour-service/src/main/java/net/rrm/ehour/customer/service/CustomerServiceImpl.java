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
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.persistence.customer.dao.CustomerDao;
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
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDAO;

    private static final Logger LOGGER = Logger.getLogger(CustomerServiceImpl.class);

    public CustomerServiceImpl() {
    }

    @Transactional(readOnly = true)
    public Customer getCustomer(String customerName, String customerCode) {
        return customerDAO.findOnNameAndCode(customerName, customerCode);
    }

    @Auditable(actionType = AuditActionType.DELETE)
    @Transactional
    public void deleteCustomer(Integer customerId) throws ParentChildConstraintException {
        Customer customer = customerDAO.findById(customerId);

        LOGGER.info("Deleting customer: " + customer);

        if (customer != null) {
            if (customer.getProjects() != null &&
                    !customer.getProjects().isEmpty()) {
                throw new ParentChildConstraintException(customer.getProjects().size() + " projects attached to client");
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

        boolean canDelete = !(customer.getProjects() != null && !customer.getProjects().isEmpty());
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
        LOGGER.info("Persisting client: " + customer);

        try {
            customerDAO.persist(customer);
        } catch (DataIntegrityViolationException cve) {
            throw new ObjectNotUniqueException(cve);
        }

        return customer;
    }

    public void setCustomerDAO(CustomerDao customerDAO) {
        this.customerDAO = customerDAO;
    }
}
