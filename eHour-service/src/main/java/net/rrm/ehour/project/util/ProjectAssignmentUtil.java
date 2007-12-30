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

package net.rrm.ehour.project.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.dto.AssignmentStatus;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;
import net.rrm.ehour.util.EhourConstants;

/**
 * Time allotted util class
 **/

public class ProjectAssignmentUtil
{
	private	ReportAggregatedDAO		reportAggregatedDAO;

	/**
	 * Check if aggregate list is empty
	 * @param aggregates
	 * @return
	 */
	public static boolean isEmptyAggregateList(Collection<AssignmentAggregateReportElement> aggregates)
	{
		float	hours = 0f;
		
		if (aggregates != null)
		{
			for (AssignmentAggregateReportElement assignmentAggregateReportElement : aggregates)
			{
				if (assignmentAggregateReportElement.getHours() != null)
				{
					hours += assignmentAggregateReportElement.getHours().floatValue();
				}
			}
		}
		
		return hours == 0f;
	}
	
	/**
	 * Get assignment id's (TODO make it generic for DO's?)
	 * @param assignments
	 * @return
	 */
	public static List<Integer> getAssignmentIds(Collection<ProjectAssignment> assignments)
	{
		List<Integer>	ids = new ArrayList<Integer>();
		
		if (assignments != null)
		{
			for (ProjectAssignment projectAssignment : assignments)
			{
				ids.add(projectAssignment.getAssignmentId());
			}
		}
		
		return ids;
	}
	
	/**
	 * Is time allotted assignment overrun
	 * @param assignment
	 * @return
	 */
	public AssignmentStatus getAssignmentStatus(ProjectAssignment assignment)
	{
		int assignmentTypeId = assignment.getAssignmentType().getAssignmentTypeId().intValue();
		AssignmentStatus	status = null;			
		
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
			status = new AssignmentStatus();
			status.setAssignmentPhase(AssignmentStatus.IN_DATERANGE_PHASE);
		}
		
		return status;
	}
	
	/**
	 * Get the status for a fixed assignment
	 * @param assignment
	 * @return
	 */
	private AssignmentStatus getFixedAssignmentStatus(ProjectAssignment assignment)
	{
		AssignmentStatus	status = new AssignmentStatus();
		AssignmentAggregateReportElement aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		status.setAggregate(aggregate);
		
		status.setAssignmentPhase(AssignmentStatus.IN_ALLOTTED_PHASE);
		
		if (aggregate != null)
		{
			if (assignment.getAllottedHours().compareTo(aggregate.getHours().floatValue()) <= 0)
			{
				status.setAssignmentPhase(AssignmentStatus.OVER_ALLOTTED_PHASE);
			}
		}
		
		return status;
	}
	
	/**
	 * Get the status for a flex assignment
	 * @param assignment
	 * @return
	 */
	private AssignmentStatus getFlexAssignmentStatus(ProjectAssignment assignment)
	{
		AssignmentStatus	status = new AssignmentStatus();
		
		AssignmentAggregateReportElement aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		status.setAggregate(aggregate);
		
		if (aggregate != null)
		{
			if (assignment.getAllottedHours().compareTo(aggregate.getHours().floatValue()) > 0)
			{
				status.setAssignmentPhase(AssignmentStatus.IN_ALLOTTED_PHASE);
			}
			else if (aggregate.getHours().floatValue()  >= (assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue()))
			{
				status.setAssignmentPhase(AssignmentStatus.OVER_OVERRUN_PHASE);
			}
			else
			{
				status.setAssignmentPhase(AssignmentStatus.IN_OVERRUN_PHASE);
			}
		}
		else
		{
			status.setAssignmentPhase(AssignmentStatus.IN_ALLOTTED_PHASE);
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
