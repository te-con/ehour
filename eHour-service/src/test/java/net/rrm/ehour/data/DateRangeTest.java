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

import java.util.Date;

import junit.framework.TestCase;

/**
 * 
 **/
@SuppressWarnings("deprecation")
public class DateRangeTest extends TestCase
{
	public void testEquals()
	{
		Date 		da = new Date(2006 - 1900, 5, 5);
		Date 		db = new Date(2006 - 1900, 7, 5);
		Date 		dc = new Date(2006 - 1900, 6, 5);
		Date 		dd = new Date(2006 - 1900, 8, 5);
		
		DateRange	rA;
		DateRange	rB;
		
		rA = new DateRange(da, db);
		rB = new DateRange(da, db);
		
		assertTrue(rA.equals(rB));
		assertEquals(rA.hashCode(), rB.hashCode());
		
		rB = new DateRange(dc, dd);
		
		assertFalse(rA.equals(rB));
		assertTrue(rA.hashCode() != rB.hashCode());
		
		assertTrue(rA.equals(rA));
		
		assertFalse(rA.equals(new Object()));
		
		rB = new DateRange(da, null);
		assertFalse(rA.equals(new Object()));
		
		rB = new DateRange(null, da);
		assertFalse(rA.equals(new Object()));
		
		rB = new DateRange(db, da);
		assertFalse(rA.equals(new Object()));
		
		assertFalse(rA.equals(null));
		rA = new DateRange(da, null);
		assertFalse(rA.equals(rB));

		rA = new DateRange(null, da);
		assertFalse(rA.equals(rB));

		rA = new DateRange(dd, da);
		assertFalse(rA.equals(rB));

	}
}
