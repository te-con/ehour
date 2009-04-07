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

package net.rrm.ehour.user.dao;

import java.util.Collection;
import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.CustomerFoldPreferenceId;
import net.rrm.ehour.domain.User;

/**
 * CustomerFoldPreference DAO hibernate impl 
 **/

public class CustomerFoldPreferenceDAOHibernateImpl 
	extends GenericDAOHibernateImpl<CustomerFoldPreference, CustomerFoldPreferenceId> 
	implements CustomerFoldPreferenceDAO
{
	private final static String	CACHEREGION = "query.CustomerFoldPreference";
	
	public CustomerFoldPreferenceDAOHibernateImpl()
	{
		super(CustomerFoldPreference.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.user.dao.CustomerFoldPreferenceDAO#getPreferenceForUser(net.rrm.ehour.user.domain.User, java.util.List)
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerFoldPreference> getPreferenceForUser(User user, Collection<Customer> customers)
	{
		String[]	paramKeys;
		Object[]	paramValues;
		
		paramKeys = new String[]{"user", "customers"};
		paramValues = new Object[]{user, customers};

		return findByNamedQueryAndNamedParam("CustomerFoldPreference.findForUserAndCustomers", paramKeys, paramValues, true, CACHEREGION);
	}

}
