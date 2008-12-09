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

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.EhourConstants;

/**
 * Time allotted util class
 **/

public class ProjectAssignmentStatusServiceImpl implements ProjectAssignmentStatusService
{
	private	ReportAggregatedDAO	reportAggregatedDAO;
	private TimesheetDAO		timesheetDAO;
	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.status.ProjectAssignmentStatusService#getAssignmentStatus(net.rrm.ehour.domain.ProjectAssignment, net.rrm.ehour.data.DateRange)
	 */
	public ProjectAssignmentStatus getAssignmentStatus(ProjectAssignment assignment, DateRange period)
	{
		ProjectAssignmentStatus status = getAllottedStatus(assignment);
		
		addDeadlineStatusBasedOnDate(assignment, status, period);
		
		return status;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.project.status.ProjectAssignmentStatusService#getAssignmentStatus(net.rrm.ehour.domain.ProjectAssignment)
	 */
	public ProjectAssignmentStatus getAssignmentStatus(ProjectAssignment assignment)
	{
		ProjectAssignmentStatus status = getAllottedStatus(assignment);
		
		addDeadlineStatusBasedOnEntries(assignment, status);
		
		return status;
	}
	
	/**
	 * Get status for allotted assignments
	 * @param assignment
	 * @return
	 */
	private ProjectAssignmentStatus getAllottedStatus(ProjectAssignment assignment)
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
		
		return status;
	}

	/**
	 * Add status based on date
	 * @param assignment
	 * @param status
	 */
	private void addDeadlineStatusBasedOnEntries(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		if (assignment.getDateStart() != null)
		{
			List<TimesheetEntry> entries = timesheetDAO.getTimesheetEntriesBefore(assignment, assignment.getDateStart());
			
			if (entries != null && entries.size() > 0)
			{
				status.addStatus(ProjectAssignmentStatus.Status.BEFORE_START);
				return;
			}
		}

		if (assignment.getDateEnd() != null)
		{
			List<TimesheetEntry> entries = timesheetDAO.getTimesheetEntriesAfter(assignment, assignment.getDateEnd());
			
			if (entries != null && entries.size() > 0)
			{
				status.addStatus(ProjectAssignmentStatus.Status.AFTER_DEADLINE);
				return;
			}
		}
		
		status.addStatus(ProjectAssignmentStatus.Status.RUNNING);
	}
	
	/**
	 * Add status based on period
	 * @param assignment
	 * @param status
	 */
	private void addDeadlineStatusBasedOnDate(ProjectAssignment assignment, ProjectAssignmentStatus status, DateRange period)
	{
		DateRange assignmentRange = new DateRange(assignment.getDateStart(), assignment.getDateEnd());
		
		if (DateUtil.isDateRangeOverlaps(assignmentRange, period))
		{
			status.addStatus(ProjectAssignmentStatus.Status.RUNNING);
		}
		else
		{
			if (assignment.getDateStart() != null)
			{
				if (assignmentRange.getDateStart() != null
						&& period.getDateEnd().before(assignmentRange.getDateStart()))
				{
					status.addStatus(ProjectAssignmentStatus.Status.BEFORE_START);
				}
				else if (assignmentRange.getDateEnd() != null
						&& period.getDateStart().after(assignmentRange.getDateEnd()))
				{
					status.addStatus(ProjectAssignmentStatus.Status.AFTER_DEADLINE);
				}
			}
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
			float statusAggregateHours = (status.getAggregate().getHours() == null) ? 0 : status.getAggregate().getHours().floatValue();
			int compared;
			
			if (assignment.getAllottedHours() != null)
			{
				compared = assignment.getAllottedHours().compareTo(statusAggregateHours);
			}
			else
			{
				compared = new Float(0).compareTo(statusAggregateHours);
			};
			
			
			if (compared <= 0)
			{
				if (compared < 0)
				{
					status.setValid(false);
				}
				
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
			float statusAggregateHours = (status.getAggregate().getHours() == null) ? 0 : status.getAggregate().getHours().floatValue();
			
			if (assignment.getAllottedHours().compareTo(statusAggregateHours) > 0)
			{
				status.addStatus(ProjectAssignmentStatus.Status.IN_ALLOTTED);

			}
			else if (statusAggregateHours  >= (assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue()))
			{
				status.addStatus(ProjectAssignmentStatus.Status.OVER_OVERRUN);
	
				// it's still valid when it's right on the mark
				status.setValid(!status.isValid() ||  
							(statusAggregateHours  == (assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue())));
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

	/**
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDAO timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}
}
