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

import java.util.List;

import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customer service implementation 
 **/
@Service("customerService")
public class CustomerServiceImpl implements CustomerService
{
	@Autowired
	private	CustomerDAO		customerDAO;
	
	private	static final Logger	LOGGER = Logger.getLogger(CustomerServiceImpl.class);

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.customer.service.CustomerService#getCustomer(java.lang.String, java.lang.String)
	 */
	@Transactional(readOnly=true)
	public Customer getCustomer(String customerName, String customerCode)
	{
		return customerDAO.findOnNameAndCode(customerName, customerCode);
	}	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#deleteCustomer(java.lang.Integer)
	 */
	@Transactional
	@Auditable(actionType=AuditActionType.DELETE)
	public void deleteCustomer(Integer customerId) throws ParentChildConstraintException
	{
		Customer customer = customerDAO.findById(customerId);
		
		LOGGER.info("Deleting customer: " + customer);
		
		if (customer != null)
		{
			if (customer.getProjects() != null &&
				customer.getProjects().size() > 0)
			{
				throw new ParentChildConstraintException(customer.getProjects().size() + " projects attached to customer");
			}
			else
			{
				try
				{
					customerDAO.delete(customer);
				}
				catch (DataIntegrityViolationException cve)
				{
					throw new ParentChildConstraintException(cve);
				}				
			}
		}
	}


	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getCustomer(java.lang.Integer)
	 */
	@Transactional(readOnly=true)
	public Customer getCustomer(Integer customerId) throws ObjectNotFoundException
	{
		Customer customer = customerDAO.findById(customerId);
		
		if (customer == null)
		{
			throw new ObjectNotFoundException("customer id not found" + customerId);
		}
		
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.customer.service.CustomerService#getCustomerAndCheckDeletability(java.lang.Integer)
	 */
	@Transactional(readOnly=true)
	public Customer getCustomerAndCheckDeletability(Integer customerId)
	{
		Customer customer = customerDAO.findById(customerId);
		
		if (customer.getProjects() != null && customer.getProjects().size() > 0)
		{
			// okay, this is going to be pricey...
			boolean	deletable = false;
			
//			for (Project project : customer.getProjects())
//			{
//				projectService.setProjectDeletability(project);
//				
//				deletable = project.isDeletable();
//				
//				if (!deletable)
//				{
//					break;
//				}
//			}
//			
			customer.setDeletable(deletable);
		}
		else
		{
			customer.setDeletable(true);
		}
		
		return customer;
	}	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getCustomers()
	 */
	@Transactional(readOnly=true)
	public List<Customer> getCustomers()
	{
		return customerDAO.findAll();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#persistCustomer(net.rrm.ehour.project.domain.Customer)
	 */
	@Transactional
	public Customer persistCustomer(Customer customer) throws ObjectNotUniqueException
	{
		LOGGER.info("Persisting customer: " + customer);
		
		try
		{
			customerDAO.persist(customer);
		}
		catch (DataIntegrityViolationException cve)
		{
			throw new ObjectNotUniqueException(cve);
		}
		
		return customer;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.customer.service.CustomerService#getCustomers(boolean)
	 */
	@Transactional(readOnly=true)
	public List<Customer> getCustomers(boolean hideInactive)
	{
		return (hideInactive) ? customerDAO.findAllActive() : customerDAO.findAll();
	}

	
	/**
	 * @param customerDAO the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}
}
