/**
 * Created on Apr 11, 2007
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

package net.rrm.ehour.report.reports;

import java.io.Serializable;
import java.util.SortedSet;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.mail.domain.MailLogAssignment;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Project manager report 
 **/

public class ProjectManagerReport implements Serializable
{
	private static final long serialVersionUID = 1768574303126675320L;
	
	private Project									project;
	private	SortedSet<AssignmentAggregateReportElement>	aggregates;
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
			for (AssignmentAggregateReportElement aggregate : aggregates)
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
	public SortedSet<AssignmentAggregateReportElement> getAggregates()
	{
		return aggregates;
	}
	/**
	 * @param aggregates the aggregates to set
	 */
	public void setAggregates(SortedSet<AssignmentAggregateReportElement> aggregates)
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
