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

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import net.rrm.ehour.ui.common.sort.LocaleComparator.CompareType;

import org.junit.Test;

/**
 * TODO 
 **/

public class LocaleComparatorTest
{

	/**
	 * Test method for {@link net.rrm.ehour.ui.common.sort.LocaleComparator#compare(java.util.Locale, java.util.Locale)}.
	 */
	@Test
	public void testCompareCountry()
	{
		Locale l1 = Locale.CANADA;
		Locale l2 = Locale.JAPAN;
		
		assertTrue(new LocaleComparator(CompareType.COUNTRY).compare(l1, l2) < 0);
	}

	@Test
	public void testCompareLanguage()
	{
		Locale l1 = Locale.FRANCE;
		Locale l2 = Locale.ENGLISH;
		
		assertTrue(new LocaleComparator(CompareType.LANGUAGE).compare(l1, l2) > 0);
	}

}
