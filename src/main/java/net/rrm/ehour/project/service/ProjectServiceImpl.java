/**
 * Created on Nov 25, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.project.service;

import java.util.Calendar;
import java.util.List;

import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.dao.CustomerDAO;
import net.rrm.ehour.project.domain.Customer;

/**
 * TODO 
 **/

public class ProjectServiceImpl implements ProjectService
{
	private	CustomerDAO	customerDAO;
	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#deleteCustomer(java.lang.Integer)
	 */
	public void deleteCustomer(Integer customerId) throws ParentChildConstraintException
	{
		Customer customer = customerDAO.findById(customerId);
		
		if (customer != null)
		{
			if (customer.getProjects() != null &&
				customer.getProjects().size() > 0)
			{
				throw new ParentChildConstraintException(customer.getProjects().size() + " projects attached to customer");
			}
			else
			{
				customerDAO.delete(customer);
			}
		}
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getActiveProjectsForUser(java.lang.Integer, java.util.Calendar, java.util.Calendar)
	 */
	public List getActiveProjectsForUser(Integer userId, Calendar dateStart, Calendar dateEnd)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getCustomer(java.lang.Integer)
	 */
	public Customer getCustomer(Integer customerId)
	{
		return customerDAO.findById(customerId);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#getCustomers()
	 */
	public List getCustomers()
	{
		return customerDAO.findAll();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.project.service.ProjectService#persistCustomer(net.rrm.ehour.project.domain.Customer)
	 */
	public Customer persistCustomer(Customer customer)
	{
		customerDAO.persist(customer);
		
		return customer;
	}

	/**
	 * @param customerDAO the customerDAO to set
	 */
	public void setCustomerDAO(CustomerDAO customerDAO)
	{
		this.customerDAO = customerDAO;
	}

}
