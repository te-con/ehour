/**
 * Created on Dec 4, 2006
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

import java.util.List;

import net.rrm.ehour.customer.dao.CustomerDAO;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

/**
 * Customer service implementation 
 **/

public class CustomerServiceImpl implements CustomerService
{
	private	CustomerDAO		customerDAO;
	private	Logger			logger = Logger.getLogger(this.getClass());
	

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
	public void deleteCustomer(Integer customerId) throws ParentChildConstraintException
	{
		Customer customer = customerDAO.findById(customerId);
		
		logger.info("Deleting customer: " + customer);
		
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
		logger.info("Persisting customer: " + customer);
		
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
		return customerDAO.findAll(hideInactive);
	}

	
	/**
	 * @param customerDAO the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}
}
