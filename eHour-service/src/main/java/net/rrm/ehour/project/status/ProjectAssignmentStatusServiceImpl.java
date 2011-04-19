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

package net.rrm.ehour.project.status;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.persistence.report.dao.ReportAggregatedDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.EhourConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Time allotted util class
 **/
@Service("projectAssignmentStatusService")
public class ProjectAssignmentStatusServiceImpl implements ProjectAssignmentStatusService
{
	@Autowired
	private	ReportAggregatedDao	reportAggregatedDAO;
	@Autowired
	private TimesheetDao		timesheetDAO;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.project.status.ProjectAssignmentStatusService#getAssignmentStatus(net.rrm.ehour.persistence.persistence.domain.ProjectAssignment, net.rrm.ehour.persistence.persistence.data.DateRange)
	 */
	public ProjectAssignmentStatus getAssignmentStatus(ProjectAssignment assignment, DateRange period)
	{
		ProjectAssignmentStatus status = getAllottedStatus(assignment);
		
		addDeadlineStatusBasedOnDate(assignment, status, period);
		
		return status;
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.project.status.ProjectAssignmentStatusService#getAssignmentStatus(net.rrm.ehour.persistence.persistence.domain.ProjectAssignment)
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

		addStatusForAssignmentType(assignment, status);
		
		return status;
	}

	private void addStatusForAssignmentType(ProjectAssignment assignment, ProjectAssignmentStatus status)
	{
		int assignmentTypeId = assignment.getAssignmentType().getAssignmentTypeId();
		
		if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
		{
			addFixedAssignmentStatus(assignment, status);
		}
		else if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			addFlexAssignmentStatus(assignment, status);
		}
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
	 * Get the status for a fixed assignment.
	 * 
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
			}
			
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
			else if (statusAggregateHours  >= (assignment.getAllottedHours() + assignment.getAllowedOverrun()))
			{
				status.addStatus(ProjectAssignmentStatus.Status.OVER_OVERRUN);

                float totalAllowed = assignment.getAllottedHours() + assignment.getAllowedOverrun();

				// it's still valid when it's right on the mark
				status.setValid(!status.isValid() || Math.abs(statusAggregateHours - totalAllowed) < 0.0001);
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
	public void setReportAggregatedDAO(ReportAggregatedDao reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}

	/**
	 * @param timesheetDAO the timesheetDAO to set
	 */
	public void setTimesheetDAO(TimesheetDao timesheetDAO)
	{
		this.timesheetDAO = timesheetDAO;
	}
}
