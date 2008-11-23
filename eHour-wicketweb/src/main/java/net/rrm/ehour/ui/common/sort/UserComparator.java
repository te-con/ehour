/**
 * Created on Mar 19, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.common.sort;

import java.util.Comparator;

import net.rrm.ehour.domain.User;

/**
 * TODO 
 **/

public class UserComparator implements Comparator<User>
{
	private boolean firstNameFirst;
	
	/**
	 * 
	 * @param firstNameFirst
	 */
	public UserComparator(boolean firstNameFirst)
	{
		this.firstNameFirst = firstNameFirst;
	}

	/**
	 * 
	 */
	public int compare(User o1, User o2)
	{
		int	cmp;
		
		if (firstNameFirst)
		{
			cmp = o1.getFirstName().compareToIgnoreCase(o2.getFirstName());

			if (cmp == 0)
			{
				cmp = o1.getLastName().compareToIgnoreCase(o2.getLastName());
			}
		}
		else
		{
			cmp = o1.getLastName().compareToIgnoreCase(o2.getLastName());
			
			if (cmp == 0)
			{
				cmp = o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
			}
		}
		
		return cmp;
	}
}
