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

package net.rrm.ehour.timesheet.service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import net.rrm.ehour.timesheet.dto.BookedDay;

/**
 * Date comparator of booked days
 **/

public class BookedDayComparator implements Comparator<BookedDay>, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811098485423672398L;

	public int compare(BookedDay a, BookedDay b)
	{
		int			compare;
		Date		bda,
					bdb;
		
    	bda = ((BookedDay)a).getDate();
    	bdb = ((BookedDay)b).getDate();
    	
    	compare = bda.equals(bdb) ? 0 :
    			  bda.before(bdb) ? - 1 : 1; 
        
        return compare;
	}

}
