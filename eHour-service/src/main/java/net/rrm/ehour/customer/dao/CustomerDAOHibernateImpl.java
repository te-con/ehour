/**
 * Created on Nov 25, 2006
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
