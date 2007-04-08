/*
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

package net.rrm.ehour.timesheet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAO;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetCommentId;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact.
 * 
 * @author Thies
 *
 */

public class TimesheetServiceImpl implements TimesheetService
{
	private	TimesheetDAO		timesheetDAO;
	private TimesheetCommentDAO	timesheetCommentDAO;
	private	ReportService		reportService;
	private ProjectAssignmentService	projectAssignmentService;
	private	EhourConfig			configuration;
	private	Logger				logger = Logger.getLogger(TimesheetServiceImpl.class);
	
	/**
	 * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
	 * requested month and a list with all timesheet entries for that month.
	 * @param userId
	 * @param requestedMonth only the month and year of the calendar is used
	 * @return TimesheetOverviewAction
	 * @throws ObjectNotFoundException
	 */	

	public TimesheetOverview getTimesheetOverview(Integer userId, Calendar requestedMonth) 
	{
		TimesheetOverview	overview = new TimesheetOverview();
		DateRange			monthRange;
		List<TimesheetEntry> timesheetEntries = null;
		Map<Integer, List<TimesheetEntry>>	calendarMap = null;
		
		monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		logger.debug("Getting timesheet overview for userId " + userId + " in range " + monthRange);
		
		overview.setProjectStatus(getProjectStatus(userId, monthRange));
		
		timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(userId, monthRange);
		logger.debug("Timesheet entries found for userId " + userId + " in range " + monthRange + ": " + timesheetEntries.size());

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
		List<ProjectAssignmentAggregate>	aggregates;
		List<Integer>						assignmentIds = new ArrayList<Integer>();
		SortedSet<UserProjectStatus>		userProjectStatus = new TreeSet<UserProjectStatus>();
		List<ProjectAssignmentAggregate> 	timeAllottedAggregates;
		Map<Integer, ProjectAssignmentAggregate>	originalAggregates = new HashMap<Integer, ProjectAssignmentAggregate>();
		Integer 							assignmentId;
		
		aggregates = reportService.getHoursPerAssignmentInRange(userId, monthRange);
		
		logger.debug("Getting project status for " + aggregates.size() + " assignments");
		
		// only flex & fixed needed, others can already be added to the returned list
		for (ProjectAssignmentAggregate aggregate : aggregates)
		{
			if (aggregate.getProjectAssignment().getAssignmentType().isFixedAllottedType() ||
				aggregate.getProjectAssignment().getAssignmentType().isFlexAllottedType())
			{
				assignmentId = aggregate.getProjectAssignment().getAssignmentId();
				assignmentIds.add(assignmentId);
				originalAggregates.put(assignmentId, aggregate);

				logger.debug("Fetching total hours for assignment Id: " + assignmentId);
			}
			else
			{
				userProjectStatus.add(new UserProjectStatus(aggregate));
			}
		}
		
		// fetch total hours for flex/fixed assignments
		if (assignmentIds.size() > 0)
		{
			timeAllottedAggregates = reportService.getHoursPerAssignment((Integer[])assignmentIds.toArray(new Integer[assignmentIds.size()]));
			
			for (ProjectAssignmentAggregate aggregate : timeAllottedAggregates)
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
		logger.debug("Getting booked days overview for userId " + userId + " in range " + monthRange);
		
		bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);
		
		for (BookedDay bookedDay : bookedDays)
		{
			if (bookedDay.getHours().doubleValue() >= configuration.getCompleteDayHours())
			{
				bookedDaysReturn.add(bookedDay);
			}
		}
		
		logger.debug("Booked days found for userId " + userId + ": " + bookedDaysReturn.size());
		logger.debug("Total booked days found for userId " + userId + ": " + bookedDays.size());
		
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
	public WeekOverview getWeekOverview(Integer userId, Calendar requestedWeek)
	{
		WeekOverview	weekOverview;
		DateRange		range;
		
		weekOverview = new WeekOverview();
		
		requestedWeek.setFirstDayOfWeek(Calendar.SUNDAY);
		range = DateUtil.getDateRangeForWeek(requestedWeek);

		weekOverview.setWeekRange(range);
		
		weekOverview.setTimesheetEntries(timesheetDAO.getTimesheetEntriesInRange(userId, range));
		logger.debug("Week overview: timesheet entries found for userId " + userId + ": " + weekOverview.getTimesheetEntries().size());

		weekOverview.setComment(timesheetCommentDAO.findById(new TimesheetCommentId(userId, range.getDateStart())));
		logger.debug("Week overview: comments found for userId " + userId + ": " + (weekOverview.getComment() != null));
		
		weekOverview.setProjectAssignments(projectAssignmentService.getProjectAssignmentsForUser(userId, range));
		logger.debug("Week overview: project assignments found for userId " + userId + " in range " + range + ": " + weekOverview.getProjectAssignments().size());
		
		return weekOverview;
	}	

	/**
	 * Persist timesheets & comment
	 */
	public void persistTimesheet(Set<TimesheetEntry> timesheetEntries, TimesheetComment comment)
	{
		for (TimesheetEntry entry : timesheetEntries)
		{
			
			if (entry.getHours() == null)
			{
				logger.debug("Deleting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
						" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours());
				timesheetDAO.delete(entry);
				
			}
			else
			{
				logger.debug("Persisting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
						" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours());
				timesheetDAO.persist(entry);
			}
		}
		
		if (comment != null 
			&& comment.getComment() != null
			&& comment.getComment().trim().length() > 0)
		{
			logger.debug("Persisting timesheet comment for week " + comment.getCommentId().getCommentDate());
			timesheetCommentDAO.persist(comment);
		}
		
		checkTimeAllottedOverruns(timesheetEntries);
	}
	
	/**
	 * Check for time allotted overruns
	 * @param timesheetEntries
	 */
	private void checkTimeAllottedOverruns(Set<TimesheetEntry> timesheetEntries)
	{
		Set<ProjectAssignment>	projectAssignments = new HashSet<ProjectAssignment>();
		
		for (TimesheetEntry entry : timesheetEntries)
		{
			projectAssignments.add(entry.getEntryId().getProjectAssignment());
		}
		
		projectAssignmentService.checkForOverruns(projectAssignments);
	}
	
		
	
	/**
	 * DAO setter (Spring)
	 * @param dao
	 */
	public void setTimesheetDAO(TimesheetDAO dao)
	{
		timesheetDAO = dao;
	}

	/**
	 * ReportData setter (Spring)
	 * @param dao
	 */	
	
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
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
	public void setTimesheetCommentDAO(TimesheetCommentDAO timesheetCommentDAO)
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
}
