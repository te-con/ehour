/**
 * Created on Jun 30, 2007
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

		l = getHibernateTemplate().findByNamedQueryAndNamedParam("CustomerFoldPreference.findForUserAndCustomers", paramKeys, paramValues);
		
		return l;
	}

}
