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

package net.rrm.ehour.ui.report.panel.criteria.quick;

import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.Localizer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.easymock.EasyMock.*;

public class QuickWeekRendererTest extends BaseSpringWebAppTester
{
	QuickWeekRenderer renderer;
	Localizer			localizer;
	
	@Before
	public void before()
	{
		localizer = createMock(Localizer.class); 
		
		renderer = new QuickWeekRenderer(new EhourConfigStub())
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 8564023232543242798L;

			protected Localizer getLocalizer()
			{
				return localizer;
			}
		};
	}

	/**
	 * Test method for {@link net.rrm.ehour.ui.report.panel.criteria.quick.QuickWeekRenderer#getDisplayValue(java.lang.Object)}.
	 */
	@Test
	public void testGetDisplayValueCurrent()
	{
		QuickWeek week = new QuickWeek(Calendar.getInstance(), new EhourConfigStub());
		
		expect(localizer.getString("report.criteria.currentWeek", null)).andReturn("");
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
					.andReturn("");
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
			.andReturn("");
		replay(localizer);		
		
		renderer.getDisplayValue(week);
		
		verify(localizer);
	}

	@Test
	public void testPayAttentionToFirstDayOfWeek() {
		Locale.setDefault(Locale.ITALIAN);
		Calendar c = new GregorianCalendar(2011, 7 -1, 5); //Tuesday
		Assert.assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
		c.add(Calendar.WEEK_OF_YEAR, -1);

		Calendar c2 = (Calendar)c.clone();
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		//the next step will change week of year
		c2.setFirstDayOfWeek(Calendar.SUNDAY);
		Assert.assertFalse(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

		//reversing the operation works as expected
		Assert.assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
		c.add(Calendar.WEEK_OF_YEAR, -1);

		c2 = (Calendar)c.clone();
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.setFirstDayOfWeek(Calendar.SUNDAY);
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Assert.assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

		// Executing the same test moving First day of week forward works always as expected
		c = new GregorianCalendar(2011, 7 -1, 5); //Tuesday
		Assert.assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
		c.add(Calendar.WEEK_OF_YEAR, -1);

		c2 = (Calendar)c.clone();
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		//the next step will NOT change week of year
		c2.setFirstDayOfWeek(Calendar.SUNDAY);
		Assert.assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

		//reversing the operation works as expected
		Assert.assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
		c.add(Calendar.WEEK_OF_YEAR, -1);

		c2 = (Calendar)c.clone();
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.setFirstDayOfWeek(Calendar.WEDNESDAY);
		Assert.assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

		c2.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		Assert.assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

}
}
