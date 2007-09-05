/**
 * Created on Nov 15, 2006
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

import java.util.List;

import net.rrm.ehour.dao.GenericDAOHibernateImpl;
import net.rrm.ehour.user.domain.UserDepartment;

/**
 *  
 **/

public class UserDepartmentDAOHibernateImpl 
	extends GenericDAOHibernateImpl<UserDepartment, Integer> implements UserDepartmentDAO
{
	private final static String CACHEREGION = "query.Department";
	
	public UserDepartmentDAOHibernateImpl()
	{
		super(UserDepartment.class);
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public UserDepartment findOnNameAndCode(String name, String code)
	{
		List<UserDepartment>		results;

		String[]	keys = new String[]{"name", "code"};
		Object[]	params = new Object[]{name.toLowerCase(), code.toLowerCase()}; 
		
		results = findByNamedQueryAndNamedParam("UserDepartment.findByNameAndCode"
													, keys, params, true, CACHEREGION);		
		
		return (results.size() > 0) ? results.get(0) : null;
	}	
}
