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
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;

import java.util.List;

/**
 * Customer service, can I help you ?
 */

public interface CustomerService {
    /**
     * Get all customers regardless whether they are active or not
     *
     * @return
     */
    public List<Customer> getCustomers();

    /**
     * Get all active customers
     */
    public List<Customer> getActiveCustomers();

    /**
     * Get customer on id and check deletability
     *
     * @param customerId
     * @return
     */
    public Customer getCustomerAndCheckDeletability(Integer customerId) throws ObjectNotFoundException;

    /**
     * Get customer on id
     *
     * @param customerId
     * @return
     */
    public Customer getCustomer(Integer customerId);

    /**
     * Get customer on name and code
     *
     * @param customerName
     * @param customerCode
     * @return
     */
    public Customer getCustomer(String customerName, String customerCode);

    /**
     * Delete customer
     *
     * @param customerId
     */
    public void deleteCustomer(Integer customerId) throws ParentChildConstraintException;

    /**
     * Persist customer
     *
     * @param customer
     * @return
     */
    public Customer persistCustomer(Customer customer) throws ObjectNotUniqueException;
}
