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

package net.rrm.ehour.persistence.customer.dao;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.persistence.dao.GenericDao;

import java.util.List;

/**
 * CRUD on the Customer domain object 
 **/

public interface CustomerDao extends GenericDao<Customer, Integer>
{

    /**
	 * Get all customers
	 * @param active
	 * @return
	 */
	List<Customer> findAll();
	
	/**
	 * Find all active customers
	 * @return
	 */
	List<Customer> findAllActive();
	
	/**
	 * Find customer on name and code
	 * @param name
	 * @param code
	 * @return
	 */
	Customer findOnNameAndCode(String name, String code);
}
