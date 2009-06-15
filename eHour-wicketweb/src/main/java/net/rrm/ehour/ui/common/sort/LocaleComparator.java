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

package net.rrm.ehour.ui.common.sort;

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
