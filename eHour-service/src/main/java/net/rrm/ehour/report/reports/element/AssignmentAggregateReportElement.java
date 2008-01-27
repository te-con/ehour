/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.report.reports.element;

import java.util.Date;

import net.rrm.ehour.domain.ProjectAssignment;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * ReportElement for aggregate reports
 **/

public class AssignmentAggregateReportElement 
			extends ReportElement 
			implements Comparable<AssignmentAggregateReportElement>
{
	private static final long serialVersionUID = -7175763322632066925L;
	private ProjectAssignment 	projectAssignment;

	
	/**
	 * default construct
	 */
	public AssignmentAggregateReportElement()
	{
		
	}
	
	/**
	 * full constructor
	 * @param projectAssignment
	 * @param hours
	 */
	public AssignmentAggregateReportElement(ProjectAssignment projectAssignment, Number hours)
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
	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}

	/**
	 * 
	 * @param projectAssignment
	 */
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
	public int compareTo(AssignmentAggregateReportElement pagO)
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
		if (!(object instanceof AssignmentAggregateReportElement))
		{
			return false;
		}
		AssignmentAggregateReportElement rhs = (AssignmentAggregateReportElement) object;
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
