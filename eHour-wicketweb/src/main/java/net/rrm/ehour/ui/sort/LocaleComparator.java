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

package net.rrm.ehour.ui.sort;

import java.util.Comparator;
import java.util.Locale;

/**
 *  
 **/

public class LocaleComparator implements Comparator<Locale>
{
	public enum CompareType { COUNTRY, LANGUAGE };

	private CompareType selectedCompareType;
	
	public LocaleComparator(CompareType compareType)
	{
		this.selectedCompareType = compareType;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Locale l1, Locale l2)
	{
		if (selectedCompareType == CompareType.COUNTRY)
		{
			return l1.getDisplayCountry().compareTo(l2.getDisplayCountry());
		}
		else
		{
			return l1.getDisplayName().compareTo(l2.getDisplayName());
		}
	}

}
