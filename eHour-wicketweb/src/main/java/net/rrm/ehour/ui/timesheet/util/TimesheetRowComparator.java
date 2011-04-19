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

package net.rrm.ehour.ui.timesheet.util;

import net.rrm.ehour.ui.timesheet.dto.TimesheetRow;

import java.util.Comparator;


/**
 * TODO 
 **/

public class TimesheetRowComparator implements Comparator<TimesheetRow>
{
    public static final TimesheetRowComparator INSTANCE = new TimesheetRowComparator();

    private TimesheetRowComparator()
    {

    }

	public int compare(TimesheetRow o1, TimesheetRow o2)
	{
		return o1.getProjectAssignment().getProject().getName().compareToIgnoreCase(
				o2.getProjectAssignment().getProject().getName());
	}

}
