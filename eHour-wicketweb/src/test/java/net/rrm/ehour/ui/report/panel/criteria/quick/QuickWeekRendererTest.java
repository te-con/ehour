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
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuickWeekRendererTest extends BaseSpringWebAppTester {
    QuickWeekRenderer renderer;
    Localizer localizer;

    @Before
    public void before() {
        localizer = mock(Localizer.class);

        renderer = new QuickWeekRenderer(new EhourConfigStub()) {
            protected Localizer getLocalizer() {
                return localizer;
            }
        };
    }

    @Test
    public void should_display_current_week() {
        QuickWeek week = new QuickWeek(Calendar.getInstance(), new EhourConfigStub());

        when(localizer.getString("report.criteria.currentWeek", null)).thenReturn("");

        renderer.getDisplayValue(week);

        verify(localizer).getString("report.criteria.currentWeek", null);
    }

    @Test
    public void should_display_previous() {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.WEEK_OF_YEAR, -1);

        QuickWeek week = new QuickWeek(c, new EhourConfigStub());

        when(localizer.getString("report.criteria.previousWeek", null)).thenReturn("");
        renderer.getDisplayValue(week);

        verify(localizer).getString("report.criteria.previousWeek", null);
    }


    @Test
    public void should_display_next() {
        Calendar c = new GregorianCalendar();
        c.add(Calendar.WEEK_OF_YEAR, +1);

        QuickWeek week = new QuickWeek(c, new EhourConfigStub());

        when(localizer.getString("report.criteria.nextWeek", null)).thenReturn("");
        renderer.getDisplayValue(week);

        verify(localizer).getString("report.criteria.nextWeek", null);
    }

    @Test
    public void should_respect_first_day_of_week() {
        Locale.setDefault(Locale.ITALIAN);
        Calendar c = new GregorianCalendar(2011, 7 - 1, 5); //Tuesday
        assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
        c.add(Calendar.WEEK_OF_YEAR, -1);

        Calendar c2 = (Calendar) c.clone();
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        //the next step will change week of year
        c2.setFirstDayOfWeek(Calendar.SUNDAY);
        assertFalse(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

        //reversing the operation works as expected
        assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
        c.add(Calendar.WEEK_OF_YEAR, -1);

        c2 = (Calendar) c.clone();
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.setFirstDayOfWeek(Calendar.SUNDAY);
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

        // Executing the same test moving First day of week forward works always as expected
        c = new GregorianCalendar(2011, 7 - 1, 5); //Tuesday
        assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
        c.add(Calendar.WEEK_OF_YEAR, -1);

        c2 = (Calendar) c.clone();
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        //the next step will NOT change week of year
        c2.setFirstDayOfWeek(Calendar.SUNDAY);
        assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));

        //reversing the operation works as expected
        assertEquals(Calendar.MONDAY, c.getFirstDayOfWeek());
        c.add(Calendar.WEEK_OF_YEAR, -1);

        c2 = (Calendar) c.clone();
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.setFirstDayOfWeek(Calendar.WEDNESDAY);
        assertEquals(c.get(Calendar.WEEK_OF_YEAR), c2.get(Calendar.WEEK_OF_YEAR));

        c2.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        assertTrue(c.get(Calendar.WEEK_OF_YEAR) == c2.get(Calendar.WEEK_OF_YEAR));
    }
}
