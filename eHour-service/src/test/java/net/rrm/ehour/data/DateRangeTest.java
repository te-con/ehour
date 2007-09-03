/**
 * Created on Jan 12, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.data;

import java.util.Date;

import junit.framework.TestCase;

/**
 * TODO 
 **/

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
