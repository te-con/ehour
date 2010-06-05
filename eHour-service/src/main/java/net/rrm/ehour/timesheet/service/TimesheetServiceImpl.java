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

package net.rrm.ehour.timesheet.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.timesheet.dao.TimesheetDao;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.EhourUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact. 
 * @author Thies
 *
 */
@Service("timesheetService")
public class TimesheetServiceImpl implements TimesheetService
{
	@Autowired
	private	TimesheetDao		timesheetDAO;

	@Autowired
	private TimesheetCommentDao	timesheetCommentDAO;
	
	@Autowired
	private	AggregateReportService		aggregateReportService;
	
	@Autowired
	private ProjectAssignmentService	projectAssignmentService;
	
	@Autowired
	private	EhourConfig			configuration;
	
	@Autowired
	private TimesheetPersister	timesheetPersister;
	

	private	static final Logger	LOGGER = Logger.getLogger(TimesheetServiceImpl.class);

	/**
	 * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
	 * requested month and a list with all timesheet entries for that month.
	 * @param userId
	 * @param requestedMonth only the month and year of the calendar is used
	 * @return TimesheetOverviewAction
	 * @throws ObjectNotFoundException
	 */	
	public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth) 
	{
		TimesheetOverview	overview = new TimesheetOverview();
		DateRange			monthRange;
		List<TimesheetEntry> timesheetEntries = null;
		Map<Integer, List<TimesheetEntry>>	calendarMap = null;
		
		monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		LOGGER.debug("Getting timesheet overview for userId " + user.getUserId() + " in range " + monthRange);
		
		overview.setProjectStatus(getProjectStatus(user.getUserId(), monthRange));
		
		timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), monthRange);
		LOGGER.debug("Timesheet entries found for userId " + user.getUserId() + " in range " + monthRange + ": " + timesheetEntries.size());

		calendarMap = entriesToCalendarMap(timesheetEntries);
		overview.setTimesheetEntries(calendarMap);
		
		return overview;
	}
	
	/**
	 * Get project status for user
	 * @param userId
	 * @param monthRange
	 * @return
	 */
	private SortedSet<UserProjectStatus> getProjectStatus(Integer userId, DateRange monthRange)
	{
		List<AssignmentAggregateReportElement>	aggregates;
		List<Serializable>						assignmentIds = new ArrayList<Serializable>();
		SortedSet<UserProjectStatus>		userProjectStatus = new TreeSet<UserProjectStatus>();
		List<AssignmentAggregateReportElement> 	timeAllottedAggregates;
		Map<Integer, AssignmentAggregateReportElement>	originalAggregates = new HashMap<Integer, AssignmentAggregateReportElement>();
		Integer 							assignmentId;
		
		aggregates = aggregateReportService.getHoursPerAssignmentInRange(userId, monthRange);
		
		LOGGER.debug("Getting project status for " + aggregates.size() + " assignments");
		
		// only flex & fixed needed, others can already be added to the returned list
		for (AssignmentAggregateReportElement aggregate : aggregates)
		{
			if (aggregate.getProjectAssignment().getAssignmentType().isFixedAllottedType() ||
				aggregate.getProjectAssignment().getAssignmentType().isFlexAllottedType())
			{
				assignmentId = aggregate.getProjectAssignment().getAssignmentId();
				assignmentIds.add(assignmentId);
				originalAggregates.put(assignmentId, aggregate);

				LOGGER.debug("Fetching total hours for assignment Id: " + assignmentId);
			}
			else
			{
				userProjectStatus.add(new UserProjectStatus(aggregate));
			}
		}
		
		// fetch total hours for flex/fixed assignments
		if (assignmentIds.size() > 0)
		{
			timeAllottedAggregates = aggregateReportService.getHoursPerAssignment(assignmentIds);
			
			for (AssignmentAggregateReportElement aggregate : timeAllottedAggregates)
			{
				userProjectStatus.add(new UserProjectStatus(originalAggregates.get(aggregate.getProjectAssignment().getAssignmentId()),
																					aggregate.getHours()));
			}
		}
		
		return userProjectStatus;
	}
	
	/**
	 * Get a list with all day numbers in this month that has complete booked days (config defines the completion
	 * level). 
	 * @param userId
	 * @param requestedMonth
	 * @return List with Integers of complete booked days
	 * @throws ObjectNotFoundException
	 */
	public List<BookedDay> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth)
	{
		DateRange		monthRange;
		List<BookedDay>	bookedDays;
		List<BookedDay>	bookedDaysReturn = new ArrayList<BookedDay>();
		
		monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		LOGGER.debug("Getting booked days overview for userId " + userId + " in range " + monthRange);
		
		bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);
		
		for (BookedDay bookedDay : bookedDays)
		{
			if (bookedDay.getHours() != null &&
					bookedDay.getHours().floatValue() >= configuration.getCompleteDayHours())
			{
				bookedDaysReturn.add(bookedDay);
			}
		}
		
		LOGGER.debug("Booked days found for userId " + userId + ": " + bookedDaysReturn.size());
		LOGGER.debug("Total booked days found for userId " + userId + ": " + bookedDays.size());
		
		Collections.sort(bookedDaysReturn, new BookedDayComparator());
		
		return bookedDaysReturn;
	}		
	
	/**
	 * Put the timesheet entries in a map where the day is the key and
	 * the value is a list of timesheet entries filled out for that date
	 * @param timesheetEntries
	 * @return
	 */
	private Map<Integer, List<TimesheetEntry>> entriesToCalendarMap(List<TimesheetEntry> timesheetEntries)
	{
		Map<Integer, List<TimesheetEntry>>	calendarMap;
		Calendar							cal;
		Integer								dayKey;
		List<TimesheetEntry> 				dayEntries;
		
		calendarMap = new HashMap<Integer, List<TimesheetEntry>>();
		
		for (TimesheetEntry entry: timesheetEntries)
		{
			if (entry == null)
			{
				continue;
			}
				
           cal = new GregorianCalendar();
           cal.setTime(entry.getEntryId().getEntryDate());
           
           dayKey = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH));

           if (calendarMap.containsKey(dayKey))
           {
               dayEntries = calendarMap.get(dayKey);
           }
           else
           {
               dayEntries = new ArrayList<TimesheetEntry>();
           }

           dayEntries.add(entry);

           calendarMap.put(dayKey, dayEntries);
        }		
		
		return calendarMap;
	}
	
	/**
	 * Get week overview for a date. Week number of supplied requested week is used
	 * @param userId
	 * @param requestedWeek
	 * @return
	 */
	public WeekOverview getWeekOverview(User user, Calendar requestedWeek, EhourConfig config)
	{
		WeekOverview	weekOverview;
		DateRange		range;

		weekOverview = new WeekOverview();
		
		requestedWeek.setFirstDayOfWeek(config.getFirstDayOfWeek());
		range = DateUtil.getDateRangeForWeek(requestedWeek);

		weekOverview.setWeekRange(range);
		
		weekOverview.setTimesheetEntries(timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), range));
		LOGGER.debug("Week overview: timesheet entries found for userId " + user.getUserId() + ": " + weekOverview.getTimesheetEntries().size());

		weekOverview.setComment(timesheetCommentDAO.findById(new TimesheetCommentId(user.getUserId(), range.getDateStart())));
		LOGGER.debug("Week overview: comments found for userId " + user.getUserId() + ": " + (weekOverview.getComment() != null));

		weekOverview.setProjectAssignments(projectAssignmentService.getProjectAssignmentsForUser(user.getUserId(), range));
		LOGGER.debug("Week overview: project assignments found for userId " + user.getUserId() + " in range " + range + ": " + weekOverview.getProjectAssignments().size());
		
		weekOverview.initCustomers();
		
		weekOverview.setUser(user);
		
		return weekOverview;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetService#persistTimesheetWeek(java.util.Collection, net.rrm.ehour.domain.TimesheetComment, net.rrm.ehour.data.DateRange)
	 */
	@Transactional
	public List<ProjectAssignmentStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries, 
																TimesheetComment comment,
																DateRange weekRange)
	{
		Map<ProjectAssignment, List<TimesheetEntry>> timesheetRows = getTimesheetAsRows(timesheetEntries);

		List<ProjectAssignmentStatus> errorStatusses = new ArrayList<ProjectAssignmentStatus>();
		
		for (ProjectAssignment assignment : timesheetRows.keySet())
		{
			try
			{
				timesheetPersister.validateAndPersist(assignment, timesheetRows.get(assignment), weekRange);
			} catch (OverBudgetException e)
			{
				errorStatusses.add( e.getStatus());
			}
		}
		
		if (comment.getNewComment() == Boolean.FALSE ||
				!StringUtils.isBlank(comment.getComment()))
		{
			LOGGER.debug("Persisting timesheet comment for week " + comment.getCommentId().getCommentDate());
			timesheetCommentDAO.persist(comment);
		}
		
		return errorStatusses;
	}
	
	/**
	 * Get timesheet as rows
	 * @param entries
	 * @return
	 */
	private Map<ProjectAssignment, List<TimesheetEntry>> getTimesheetAsRows(Collection<TimesheetEntry> entries)
	{
		Map<ProjectAssignment, List<TimesheetEntry>> timesheetRows = new HashMap<ProjectAssignment, List<TimesheetEntry>>();
		
		for (TimesheetEntry timesheetEntry : entries)
		{
			ProjectAssignment assignment = timesheetEntry.getEntryId().getProjectAssignment();
			
			List<TimesheetEntry> assignmentEntries = (timesheetRows.containsKey(assignment)) 
															? timesheetRows.get(assignment) 
															: new ArrayList<TimesheetEntry>();
															
			assignmentEntries.add(timesheetEntry);
			
			timesheetRows.put(assignment, assignmentEntries);
		}
		
		return timesheetRows; 
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetService#deleteTimesheetEntries(net.rrm.ehour.user.domain.User)
	 */
	@Transactional
	public void deleteTimesheetEntries(User user)
	{
		timesheetCommentDAO.deleteCommentsForUser(user.getUserId());
		
		if (user.getProjectAssignments() != null && user.getProjectAssignments().size() > 0)
		{
			timesheetDAO.deleteTimesheetEntries(EhourUtil.getIdsFromDomainObjects(user.getProjectAssignments()));
		}
	}
		
	
	/**
	 * DAO setter (Spring)
	 * @param dao
	 */
	public void setTimesheetDAO(TimesheetDao dao)
	{
		timesheetDAO = dao;
	}

	/**
	 * ReportData setter (Spring)
	 * @param dao
	 */	
	
	public void setReportService(AggregateReportService aggregateReportService)
	{
		this.aggregateReportService = aggregateReportService;
	}
	
	/**
	 * Setter for the config
	 * @param config
	 */
	public void setEhourConfig(EhourConfig config)
	{
		this.configuration = config;
	}

	/**
	 * @param timesheetCommentDAO the timesheetCommentDAO to set
	 */
	public void setTimesheetCommentDAO(TimesheetCommentDao timesheetCommentDAO)
	{
		this.timesheetCommentDAO = timesheetCommentDAO;
	}

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectAssignmentService(ProjectAssignmentService projectAssignmentService)
	{
		this.projectAssignmentService = projectAssignmentService;
	}

	/**
	 * @param aggregateReportService the aggregateReportService to set
	 */
	public void setAggregateReportService(AggregateReportService aggregateReportService)
	{
		this.aggregateReportService = aggregateReportService;
	}

	/**
	 * @param timesheetPersister the timesheetPersister to set
	 */
	public void setTimesheetPersister(TimesheetPersister timesheetPersister)
	{
		this.timesheetPersister = timesheetPersister;
	}
}
