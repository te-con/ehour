/**
 * Created on Apr 8, 2007
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

package net.rrm.ehour.timesheet.dto;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;

/**
 * Value object for timesheet overview. While the hours in an aggregate
 * reflect only a certain period, totalBookedHours is all hours booked on this assignment
 **/

public class UserProjectStatus extends ProjectAssignmentAggregate
{
	private Number	totalBookedHours;

	public UserProjectStatus()
	{
		
	}
	
	public UserProjectStatus(ProjectAssignmentAggregate aggregate)
	{
		this(aggregate, null);
	}
	
	public UserProjectStatus(ProjectAssignmentAggregate aggregate, Number totalBookedHours)
	{
		super(aggregate.getProjectAssignment(), aggregate.getHours(), aggregate.getTurnOver());
		this.totalBookedHours = totalBookedHours;
	}
	
	/**
	 * Get hours remaining to book on this project
	 * @return
	 */
	public Float getHoursRemaining()
	{
		Float	remainder = null;
		
		if (totalBookedHours != null)
		{
			if (getProjectAssignment().getAssignmentType().isFixedAllottedType())
			{
				remainder = new Float(getProjectAssignment().getAllottedHours().floatValue() -
										totalBookedHours.floatValue());
				
				
			}
			else if (getProjectAssignment().getAssignmentType().isFlexAllottedType())
			{
				remainder = new Float(getProjectAssignment().getAllottedHours().floatValue() +
										getProjectAssignment().getAllowedOverrun().floatValue() -
										totalBookedHours.floatValue());
			}
		}
		
		return remainder;
	}
	
	/**
	 * @return the totalBookedHours
	 */
	public Number getTotalBookedHours()
	{
		return totalBookedHours;
	}

	/**
	 * @param totalBookedHours the totalBookedHours to set
	 */
	public void setTotalBookedHours(Number totalBookedHours)
	{
		this.totalBookedHours = totalBookedHours;
	}
	
}
