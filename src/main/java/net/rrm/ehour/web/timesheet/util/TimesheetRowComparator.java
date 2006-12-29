/**
 * Created on Dec 29, 2006
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

package net.rrm.ehour.web.timesheet.util;

import java.io.Serializable;
import java.util.Comparator;

import net.rrm.ehour.web.timesheet.dto.TimesheetRow;

/**
 * Compare two timesheet rows
 * Default assignments go first, otherwise sort it on the project's fullname
 **/

public class TimesheetRowComparator implements Comparator<TimesheetRow>, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7649730355810739633L;

	/**
	 * Compare two timesheet rows
	 * 
	 */
	public int compare(TimesheetRow o1, TimesheetRow o2)
	{
		int	compare;
		
		boolean b = o1.getProjectAssignment().isDefaultAssignment() ^ o2.getProjectAssignment().isDefaultAssignment();

		if (b)
		{
			compare = o1.getProjectAssignment().isDefaultAssignment() ? -1 : 1;
		}
		else
		{
			compare = o1.getProjectAssignment().getProject().getFullname().compareTo(o2.getProjectAssignment().getProject().getFullname());
		}
		
		return compare;
	}

}
