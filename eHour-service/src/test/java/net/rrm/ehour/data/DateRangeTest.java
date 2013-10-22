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

package net.rrm.ehour.data;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;


@SuppressWarnings("deprecation")
public class DateRangeTest {
    @Test
    public void two_ranges_should_be_equal() {
        Date da = new Date(2006 - 1900, 5, 5);
        Date db = new Date(2006 - 1900, 7, 5);
        Date dc = new Date(2006 - 1900, 6, 5);
        Date dd = new Date(2006 - 1900, 8, 5);

        DateRange rA;
        DateRange rB;

        rA = new DateRange(da, db);
        rB = new DateRange(da, db);

        assertTrue(rA.equals(rB));
        assertEquals(rA.hashCode(), rB.hashCode());

        rB = new DateRange(dc, dd);

        assertFalse(rA.equals(rB));
        assertTrue(rA.hashCode() != rB.hashCode());

        assertTrue(rA.equals(rA));

        assertFalse(rA.equals(new Object()));

        assertFalse(rA.equals(new Object()));

        assertFalse(rA.equals(new Object()));

        rB = new DateRange(db, da);
        assertFalse(rA.equals(new Object()));

        assertNotNull(rA);
        rA = new DateRange(da, null);
        assertFalse(rA.equals(rB));

        rA = new DateRange(null, da);
        assertFalse(rA.equals(rB));

        rA = new DateRange(dd, da);
        assertFalse(rA.equals(rB));
    }

    @Test
    public void should_initialize_from_interval() {
        DateTime start = new DateTime(2012, 1, 2, 11, 15, 0, 0);
        DateTime end = new DateTime(2012, 1, 7, 11, 15, 0, 0);

        Interval interval = new Interval(start, end);

        DateRange range = new DateRange(interval);
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(range.getDateStart());

        assertEquals(2, startCal.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, startCal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, startCal.get(Calendar.MINUTE));
        assertEquals(0, startCal.get(Calendar.SECOND));

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(range.getDateEnd());

        assertEquals(7, endCal.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, endCal.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, endCal.get(Calendar.MINUTE));
        assertEquals(59, endCal.get(Calendar.SECOND));
    }
}
