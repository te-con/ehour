/**
 * Created on Jun 30, 2007
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

package net.rrm.ehour.user.dao;

import java.util.Collection;
import java.util.List;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.user.domain.CustomerFoldPreference;
import net.rrm.ehour.user.domain.CustomerFoldPreferenceId;
import net.rrm.ehour.user.domain.User;

/**
 * CustomerFoldPreference DAO hibernate impl 
 **/

public class CustomerFoldPreferenceDAOHibernateImpl 
	extends GenericDAOHibernateImpl<CustomerFoldPreference, CustomerFoldPreferenceId> 
	implements CustomerFoldPreferenceDAO
{
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
		List<CustomerFoldPreference>		l;
		
		paramKeys = new String[]{"user", "customers"};
		paramValues = new Object[]{user, customers};
		
		System.out.println("XX: " + this.getSession().isConnected());
		System.out.println("HC: " + this.getSession().hashCode());
		
		l = getHibernateTemplate().findByNamedQueryAndNamedParam("CustomerFoldPreference.findForUserAndCustomers", paramKeys, paramValues);
		
		return l;
	}

}
