/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.report.reports;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.project.domain.ProjectAssignment;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * TODO 
 **/

public class ProjectAssignmentAggregate implements Comparable<ProjectAssignmentAggregate>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7175763322632066925L;
	private ProjectAssignment 	projectAssignment;
	private Number 				hours;

	/**
	 * empty constructor
	 *
	 */
	public ProjectAssignmentAggregate()
	{
	}

	/**
	 * full constructor
	 * @param projectAssignment
	 * @param hours
	 */
	public ProjectAssignmentAggregate(ProjectAssignment projectAssignment, Number hours)
	{
		this.hours = hours;
		this.projectAssignment = projectAssignment;
	}

	/**
	 * Get the progress (booked hours) in percentage of the allotted hours, leaving out the overrun
	 * or for date ranges use the current date vs start & end date (if they're both null)
	 * @return
	 */
	public float getProgressPercentage()
	{
		float	percentage = 0;
		float	currentTime;
		float	dateRangeLength;
		
		if (projectAssignment.getAssignmentType().isAllottedType())
		{
			if (hours != null && 
				projectAssignment.getAllottedHours() != null &&
				hours.floatValue() > 0 &&
				projectAssignment.getAllottedHours().floatValue() > 0)
			{
				percentage = (hours.floatValue() / projectAssignment.getAllottedHours().floatValue()) * 100; 
			}
		}
		else if (projectAssignment.getAssignmentType().isDateType())
		{
			if (projectAssignment.getDateStart() != null &&
				projectAssignment.getDateEnd() != null)
			{
				currentTime = new Date().getTime() - projectAssignment.getDateStart().getTime();
				
				dateRangeLength = projectAssignment.getDateEnd().getTime() - 
									projectAssignment.getDateStart().getTime();
			
				percentage = (currentTime / dateRangeLength) * 100;
				
				// if percentage is above 100 for daterange the user can't book anymore hours
				// so don't display more than 100%
				if (percentage > 100)
				{
					percentage = 100;
				}
			}
		}
		
		return percentage;
	}
	
	/**
	 * For flex/fixed allotted, give the available hours
	 * @return
	 */
	public float getAvailableHours()
	{
		float	available = 0;
		
		if (projectAssignment.getAssignmentType().isFixedAllottedType())
		{
			if (hours != null && 
				projectAssignment.getAllottedHours() != null &&
				hours.floatValue() > 0 &&
				projectAssignment.getAllottedHours().floatValue() > 0)
			{
				available = projectAssignment.getAllottedHours().floatValue() - hours.floatValue();
			}
		}
		else if (projectAssignment.getAssignmentType().isFlexAllottedType())
		{
			if (hours != null && 
					projectAssignment.getAllottedHours() != null &&
					hours.floatValue() > 0 &&
					projectAssignment.getAllottedHours().floatValue() > 0)
				{
					
					available = (projectAssignment.getAllottedHours().floatValue() +
								 ((projectAssignment.getAllowedOverrun() != null) ? projectAssignment.getAllowedOverrun().floatValue() : 0))
								 - hours.floatValue();
				}
			
		}
		
		return available;
	}
	
	/**
	 * 
	 * @return
	 */
	public Number getHours()
	{
		return hours;
	}

	public void setHours(Number hours)
	{
		this.hours = hours;
	}

	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}

	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}

	/**
	 * @return the turnOver
	 */
	public Number getTurnOver()
	{
		if (projectAssignment != null && projectAssignment.getHourlyRate() != null && hours != null)
		{
			return new Float(hours.floatValue() * projectAssignment.getHourlyRate().floatValue());
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 
	 */
	public int compareTo(ProjectAssignmentAggregate pagO)
	{
		return this.getProjectAssignment().compareTo(pagO.getProjectAssignment());
	}
	
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("ProjectAssignment", projectAssignment)
			.append("hours", hours)
			.toString();		
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof ProjectAssignmentAggregate))
		{
			return false;
		}
		ProjectAssignmentAggregate rhs = (ProjectAssignmentAggregate) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.projectAssignment, rhs.projectAssignment).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(259442803, 2067843191).appendSuper(super.hashCode()).append(this.projectAssignment).toHashCode();
	}

}
