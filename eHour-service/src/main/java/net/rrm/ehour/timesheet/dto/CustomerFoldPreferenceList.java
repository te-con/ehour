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

package net.rrm.ehour.timesheet.dto;

import java.util.ArrayList;
import java.util.Collection;

import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.CustomerFoldPreference;

/**
 * TODO 
 **/

public class CustomerFoldPreferenceList extends ArrayList<CustomerFoldPreference>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2553991446702038985L;
	
	public CustomerFoldPreferenceList()
	{
		super();
	}
	
	public CustomerFoldPreferenceList(Collection<CustomerFoldPreference> c)
	{
		super(c);
	}
	
	/**
	 * Contains customer ?
	 * @param customer
	 * @return
	 */
	public CustomerFoldPreference get(Customer customer)
	{
		for (CustomerFoldPreference pref : this)
		{
			if (pref.getFoldPreferenceId().getCustomer().equals(customer))
			{
				return pref;
			}
		}
		
		return null;
	}

}
