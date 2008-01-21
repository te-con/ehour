/**
 * Created on Jan 21, 2008
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

package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class QuickWeekRendererTest
{
	QuickWeekRenderer renderer;
	
	@Before
	public void setUp()
	{
		renderer = new QuickWeekRenderer();
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeekRenderer#getDisplayValue(java.lang.Object)}.
	 */
	@Test
	public void testGetDisplayValueCurrent()
	{
		QuickWeek week = new QuickWeek(Calendar.getInstance());
		
		String str = (String)renderer.getDisplayValue(week);
		
		assertTrue(str.startsWith("Current"));
	}

	@Test
	public void testGetDisplayValuePrevious()
	{
		Calendar c = new GregorianCalendar();
		c.add(Calendar.WEEK_OF_YEAR, -1);
		
		QuickWeek week = new QuickWeek(c);
		
		String str = (String)renderer.getDisplayValue(week);
		
		assertTrue(str.startsWith("Previous"));
	}


	@Test
	public void testGetDisplayValueNext()
	{
		Calendar c = new GregorianCalendar();
		c.add(Calendar.WEEK_OF_YEAR, +1);
		
		QuickWeek week = new QuickWeek(c);
		
		String str = (String)renderer.getDisplayValue(week);
		
		assertTrue(str.startsWith("Next"));
	}
}
