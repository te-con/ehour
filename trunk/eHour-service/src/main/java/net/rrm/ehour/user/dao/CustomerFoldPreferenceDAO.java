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

import net.rrm.ehour.dao.GenericDAO;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.CustomerFoldPreferenceId;
import net.rrm.ehour.domain.User;

/**
 * CustomerFoldPreferenceDao 
 **/

public interface CustomerFoldPreferenceDAO extends GenericDAO<CustomerFoldPreference, CustomerFoldPreferenceId>
{
	/**
	 * Find preferences for user and customers
	 * @param user
	 * @param customer
	 * @return
	 */
	public List<CustomerFoldPreference> getPreferenceForUser(User user, Collection<Customer> customers);
}
