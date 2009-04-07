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

package net.rrm.ehour.customer.dao;

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Customer;

/**
 * Customer DAO 
 **/

public class CustomerDAOHibernateImpl extends GenericDAOHibernateImpl<Customer, Integer> implements CustomerDAO
{
	private final static String	CACHEREGION = "query.Customer";
	
	/**
	 * @todo fix this a bit better
	 */
	public CustomerDAOHibernateImpl()
	{
		super(Customer.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.customer.dao.CustomerDAO#findAll(boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> findAll(boolean active)
	{
		return findByNamedQueryAndNamedParam("Customer.findAllWithActive", "active", active, false, CACHEREGION);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.customer.dao.CustomerDAO#findOnNameAndCode(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Customer findOnNameAndCode(String name, String code)
	{
		String[]	keys = new String[]{"name", "code"};
		String[]	params = new String[]{name.toLowerCase(), code.toLowerCase()};
		List<Customer>		results;
		
		results = findByNamedQueryAndNamedParam("Customer.findByNameAndCode"
				, keys
				, params
				, true
				, CACHEREGION);			
		
		if (results != null && results.size() > 0)
		{
			return results.get(0);
		}
		
		return null;
	}

}
