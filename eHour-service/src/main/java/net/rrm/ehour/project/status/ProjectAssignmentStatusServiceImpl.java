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
		int assignmentTypeId = assignment.getAssignmentType().getAssignmentTypeId().intValue();
		ProjectAssignmentStatus	status = null;			
		
		if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
		{
			status = getFixedAssignmentStatus(assignment);
		}
		else if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			status = getFlexAssignmentStatus(assignment);
		}
		else if (assignmentTypeId == EhourConstants.ASSIGNMENT_DATE)
		{
			// TODO check boundaries
			status = new ProjectAssignmentStatus();
			status.setAssignmentPhase(ProjectAssignmentStatus.IN_DATERANGE_PHASE);
		}
		
		return status;
	}
	
	/**
	 * Get the status for a fixed assignment
	 * @param assignment
	 * @return
	 */
	private ProjectAssignmentStatus getFixedAssignmentStatus(ProjectAssignment assignment)
	{
		ProjectAssignmentStatus	status = new ProjectAssignmentStatus();
		AssignmentAggregateReportElement aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		status.setAggregate(aggregate);
		
		status.setAssignmentPhase(ProjectAssignmentStatus.IN_ALLOTTED_PHASE);
		
		if (aggregate != null)
		{
			if (assignment.getAllottedHours().compareTo(aggregate.getHours().floatValue()) <= 0)
			{
				status.setAssignmentPhase(ProjectAssignmentStatus.OVER_ALLOTTED_PHASE);
			}
		}
		
		return status;
	}
	
	/**
	 * Get the status for a flex assignment
	 * @param assignment
	 * @return
	 */
	private ProjectAssignmentStatus getFlexAssignmentStatus(ProjectAssignment assignment)
	{
		ProjectAssignmentStatus	status = new ProjectAssignmentStatus();
		
		AssignmentAggregateReportElement aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		status.setAggregate(aggregate);
		
		if (aggregate != null)
		{
			if (assignment.getAllottedHours().compareTo(aggregate.getHours().floatValue()) > 0)
			{
				status.setAssignmentPhase(ProjectAssignmentStatus.IN_ALLOTTED_PHASE);
			}
			else if (aggregate.getHours().floatValue()  >= (assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue()))
			{
				status.setAssignmentPhase(ProjectAssignmentStatus.OVER_OVERRUN_PHASE);
			}
			else
			{
				status.setAssignmentPhase(ProjectAssignmentStatus.IN_OVERRUN_PHASE);
			}
		}
		else
		{
			status.setAssignmentPhase(ProjectAssignmentStatus.IN_ALLOTTED_PHASE);
		}
		
		return status;
	}
	
	/**
	 * @param reportAggregatedDAO the reportAggregatedDAO to set
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}	
}
