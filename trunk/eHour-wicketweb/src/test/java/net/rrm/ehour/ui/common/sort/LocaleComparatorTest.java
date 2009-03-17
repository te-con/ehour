/**
 * Created on Jan 20, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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
