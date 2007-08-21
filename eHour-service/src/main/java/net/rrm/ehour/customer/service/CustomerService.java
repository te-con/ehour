/**
 * Created on Dec 4, 2006
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

package net.rrm.ehour.customer.service;

import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;

/**
 * Customer service, can I help you ?
 **/

public interface CustomerService
{
	/**
	 * Get all customers regardless whether they are active or not
	 * @return
	 */
	public List<Customer> getCustomers();
	
	/**
	 * Get customers respecting their active flag
	 * @param active to true for active, false for inactive
	 * @return
	 */
	public List<Customer> getCustomers(boolean hideInactive);
	
	/**
	 * Get customer on id and check deletability
	 * @param customerId
	 * @return
	 */
	public Customer getCustomerAndCheckDeletability(Integer customerId);

	/**
	 * Get customer on id
	 * @param customerId
	 * @return
	 */
	public Customer getCustomer(Integer customerId);
	
	
	/**
	 * Delete customer
	 * @param customerId
	 */
	public void deleteCustomer(Integer customerId) throws ParentChildConstraintException;
	
	/**
	 * Persist customer
	 * @param customer
	 * @return
	 */
	public Customer persistCustomer(Customer customer)  throws ObjectNotUniqueException;
	
}
