/**
 * Created on Apr 11, 2007
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

import java.util.SortedSet;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.mail.domain.MailLogAssignment;
import net.rrm.ehour.project.domain.Project;

/**
 * Project manager report 
 **/

public class ProjectManagerReport
{
	private Project									project;
	private	SortedSet<ProjectAssignmentAggregate>	aggregates;
	private	DateRange								reportRange;
	private	SortedSet<MailLogAssignment>			sentMail;
	private	Float									totalHoursBooked;
	private	Float									totalHoursAvailable;

	/**
	 * Derive the totals
	 *
	 */
	public void deriveTotals()
	{
		float 	hours = 0;
		float	avail = 0;
		
		if (aggregates != null)
		{
			for (ProjectAssignmentAggregate aggregate : aggregates)
			{
				if (aggregate.getHours() == null)
				{
					continue;
				}
				
				hours += aggregate.getHours().floatValue();
				
				if (aggregate.getProjectAssignment().getAssignmentType().isAllottedType())
				{
					avail += aggregate.getProjectAssignment().getAllottedHours().floatValue();	
				}
			}
		}
		
		totalHoursBooked = new Float(hours);
		totalHoursAvailable = new Float(avail);
	}
	
	/**
	 * @return the aggregates
	 */
	public SortedSet<ProjectAssignmentAggregate> getAggregates()
	{
		return aggregates;
	}
	/**
	 * @param aggregates the aggregates to set
	 */
	public void setAggregates(SortedSet<ProjectAssignmentAggregate> aggregates)
	{
		this.aggregates = aggregates;
	}
	/**
	 * @return the project
	 */
	public Project getProject()
	{
		return project;
	}
	/**
	 * @param project the project to set
	 */
	public void setProject(Project project)
	{
		this.project = project;
	}
	/**
	 * @return the sentMail
	 */
	public SortedSet<MailLogAssignment> getSentMail()
	{
		return sentMail;
	}
	/**
	 * @param sentMail the sentMail to set
	 */
	public void setSentMail(SortedSet<MailLogAssignment> sentMail)
	{
		this.sentMail = sentMail;
	}
	/**
	 * @return the reportRange
	 */
	public DateRange getReportRange()
	{
		return reportRange;
	}
	/**
	 * @param reportRange the reportRange to set
	 */
	public void setReportRange(DateRange reportRange)
	{
		this.reportRange = reportRange;
	}
	/**
	 * @return the totalHoursAvailable
	 */
	public Float getTotalHoursAvailable()
	{
		return totalHoursAvailable;
	}
	/**
	 * @param totalHoursAvailable the totalHoursAvailable to set
	 */
	public void setTotalHoursAvailable(Float totalHoursAvailable)
	{
		this.totalHoursAvailable = totalHoursAvailable;
	}
	/**
	 * @return the totalHoursBooked
	 */
	public Float getTotalHoursBooked()
	{
		return totalHoursBooked;
	}
	/**
	 * @param totalHoursBooked the totalHoursBooked to set
	 */
	public void setTotalHoursBooked(Float totalHoursBooked)
	{
		this.totalHoursBooked = totalHoursBooked;
	}
}
