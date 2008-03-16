/**
 * Created on Apr 7, 2007
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

package net.rrm.ehour.project.status;

import java.util.Date;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

/**
 * Time allotted util class
 **/

public class ProjectAssignmentStatusServiceImpl implements ProjectAssignmentStatusService
{
	private	ReportAggregatedDAO		reportAggregatedDAO;
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.status.ProjectAssignmentStatusService#getAssignmentStatus(net.rrm.ehour.domain.ProjectAssignment)
	 */
	public ProjectAssignmentStatus getAssignmentStatus(ProjectAssignment assignment)
	{
		ProjectAssignmentStatus	status = new ProjectAssignmentStatus();
		AssignmentAggregateReportElement aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		status.setAggregate(aggregate);

		int assignmentTypeId = assignment.getAssignmentType().getAssignmentTypeId().intValue();
		
		if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
		{
			addFixedAssignmentStatus(assignment, status);
		}
		else if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			addFlexAssignmentStatus(assignment, status);
		}
		
		addDeadlineStatus(assignment, status);
		
		return status;
	}

	/**
	 * Add status based on date
	 * @param assignment
	 * @param status
	 */
	private void addDeadlineStatus(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		Date currentDate = new Date();
		
		if (assignment.getDateStart() != null 
				&& assignment.getDateStart().after(currentDate))
		{
			status.addStatus(ProjectAssignmentStatus.Status.BEFORE_START);
		}
		else if (assignment.getDateEnd() != null 
				&& currentDate.after(assignment.getDateEnd()))
		{
			status.addStatus(ProjectAssignmentStatus.Status.AFTER_DEADLINE);
		}
		else
		{
			status.addStatus(ProjectAssignmentStatus.Status.RUNNING);
		}
	}
	
	/**
	 * Get the status for a fixed assignment
	 * @param assignment
	 * @return
	 */
	private void addFixedAssignmentStatus(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		if (status.getAggregate() != null)
		{
			if (assignment.getAllottedHours().compareTo(status.getAggregate().getHours().floatValue()) <= 0)
			{
				status.addStatus(ProjectAssignmentStatus.Status.OVER_ALLOTTED);
			}
			else
			{
				status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);
			}
		}
		else
		{
			status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);
		}
	}
	
	/**
	 * Get the status for a flex assignment
	 * @param assignment
	 * @return
	 */
	private void addFlexAssignmentStatus(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		if (status.getAggregate() != null)
		{
			if (assignment.getAllottedHours().compareTo(status.getAggregate().getHours().floatValue()) > 0)
			{
				status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);
			}
			else if (status.getAggregate().getHours().floatValue()  >= (assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue()))
			{
				status.addStatus(ProjectAssignmentStatus.Status.OVER_OVERRUN);
			}
			else
			{
				status.addStatus(ProjectAssignmentStatus.Status.IN_OVERRUN);
			}
		}
		else
		{
			status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);
		}
	}
	
	/**
	 * @param reportAggregatedDAO the reportAggregatedDAO to set
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}	
}
