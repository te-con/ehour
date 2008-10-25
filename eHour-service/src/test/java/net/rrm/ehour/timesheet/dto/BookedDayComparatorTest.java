/**
 * Created on Nov 6, 2006
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

package net.rrm.ehour.timesheet.dto;

import java.util.Date;

import net.rrm.ehour.timesheet.service.BookedDayComparator;

import junit.framework.TestCase;

/**
 * 
 **/
@SuppressWarnings("deprecation")
public class BookedDayComparatorTest extends TestCase
{

	public void testCompare()
	{
		BookedDayComparator comp = new BookedDayComparator();
		
		BookedDay bda, bdb;
		
		bda = new BookedDay();
		bdb = new BookedDay();
		
		bda.setDate(new Date(2006 - 1900, 10, 1));
		bdb.setDate(new Date(2006 - 1900, 10, 1));
		
		assertEquals(0, comp.compare(bda, bdb));

		bdb.setDate(new Date(2006 - 1900, 10, 2));
		
		assertEquals(-1, comp.compare(bda, bdb));

		bdb.setDate(new Date(2006 - 1900, 9, 1));
		
		assertEquals(1, comp.compare(bda, bdb));
	}

}
