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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.BaseUIWicketTester;

import org.apache.wicket.Localizer;
import org.junit.Before;
import org.junit.Test;
/**
 * TODO 
 **/

public class QuickWeekRendererTest extends BaseUIWicketTester
{
	QuickWeekRenderer renderer;
	Localizer			localizer;
	
	@Before
	public void setUp()
	{
		localizer = createMock(Localizer.class); 
		
		renderer = new QuickWeekRenderer(new EhourConfigStub())
		{
			protected Localizer getLocalizer()
			{
				return localizer;
			}
		};
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeekRenderer#getDisplayValue(java.lang.Object)}.
	 */
	@Test
	public void testGetDisplayValueCurrent()
	{
		QuickWeek week = new QuickWeek(Calendar.getInstance(), new EhourConfigStub());
		
		expect(localizer.getString("report.criteria.currentWeek", null))
			.andReturn(new String());
		replay(localizer);
		
		renderer.getDisplayValue(week);
		
		verify(localizer);
	}

	@Test
	public void testGetDisplayValuePrevious()
	{
		Calendar c = new GregorianCalendar();
		c.add(Calendar.WEEK_OF_YEAR, -1);
		
		QuickWeek week = new QuickWeek(c, new EhourConfigStub());
		
		expect(localizer.getString("report.criteria.previousWeek", null))
					.andReturn(new String());
		replay(localizer);		
		
		renderer.getDisplayValue(week);
		
		verify(localizer);
	}


	@Test
	public void testGetDisplayValueNext()
	{
		Calendar c = new GregorianCalendar();
		c.add(Calendar.WEEK_OF_YEAR, +1);
		
		QuickWeek week = new QuickWeek(c, new EhourConfigStub());
		
		expect(localizer.getString("report.criteria.nextWeek", null))
			.andReturn(new String());
		replay(localizer);		
		
		renderer.getDisplayValue(week);
		
		verify(localizer);
	}
}
