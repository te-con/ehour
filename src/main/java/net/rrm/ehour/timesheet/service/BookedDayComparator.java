/**
 * Created on Nov 6, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
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
