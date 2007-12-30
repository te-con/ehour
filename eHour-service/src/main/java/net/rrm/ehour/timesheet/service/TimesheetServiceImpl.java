/*
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

package net.rrm.ehour.timesheet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAO;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetCommentId;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.CustomerFoldPreferenceList;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.user.dao.CustomerFoldPreferenceDAO;
import net.rrm.ehour.user.domain.CustomerFoldPreference;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact. 
 * @author Thies
 *
 */

public class TimesheetServiceImpl implements TimesheetService
{
	private CustomerFoldPreferenceDAO	customerFoldPreferenceDAO;
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

	public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth) 
	{
		TimesheetOverview	overview = new TimesheetOverview();
		DateRange			monthRange;
		List<TimesheetEntry> timesheetEntries = null;
		Map<Integer, List<TimesheetEntry>>	calendarMap = null;
		
		monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		logger.debug("Getting timesheet overview for userId " + user.getUserId() + " in range " + monthRange);
		
		overview.setProjectStatus(getProjectStatus(user.getUserId(), monthRange));
		
		timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), monthRange);
		logger.debug("Timesheet entries found for userId " + user.getUserId() + " in range " + monthRange + ": " + timesheetEntries.size());

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
		List<Integer>						assignmentIds = new ArrayList<Integer>();
		SortedSet<UserProjectStatus>		userProjectStatus = new TreeSet<UserProjectStatus>();
		List<AssignmentAggregateReportElement> 	timeAllottedAggregates;
		Map<Integer, AssignmentAggregateReportElement>	originalAggregates = new HashMap<Integer, AssignmentAggregateReportElement>();
		Integer 							assignmentId;
		
		aggregates = reportService.getHoursPerAssignmentInRange(userId, monthRange);
		
		logger.debug("Getting project status for " + aggregates.size() + " assignments");
		
		// only flex & fixed needed, others can already be added to the returned list
		for (AssignmentAggregateReportElement aggregate : aggregates)
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
			timeAllottedAggregates = reportService.getHoursPerAssignment(assignmentIds);
			
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
		logger.debug("Getting booked days overview for userId " + userId + " in range " + monthRange);
		
		bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);
		
		for (BookedDay bookedDay : bookedDays)
		{
			if (bookedDay.getHours() != null &&
					bookedDay.getHours().doubleValue() >= configuration.getCompleteDayHours())
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
	public WeekOverview getWeekOverview(User user, Calendar requestedWeek)
	{
		WeekOverview	weekOverview;
		DateRange		range;
		List<CustomerFoldPreference> prefs = null;
		
		weekOverview = new WeekOverview();
		
		requestedWeek.setFirstDayOfWeek(Calendar.SUNDAY);
		range = DateUtil.getDateRangeForWeek(requestedWeek);

		weekOverview.setWeekRange(range);
		
		weekOverview.setTimesheetEntries(timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), range));
		logger.debug("Week overview: timesheet entries found for userId " + user.getUserId() + ": " + weekOverview.getTimesheetEntries().size());

		weekOverview.setComment(timesheetCommentDAO.findById(new TimesheetCommentId(user.getUserId(), range.getDateStart())));
		logger.debug("Week overview: comments found for userId " + user.getUserId() + ": " + (weekOverview.getComment() != null));

		weekOverview.setProjectAssignments(projectAssignmentService.getProjectAssignmentsForUser(user.getUserId(), DateUtil.getInversedDateRangeForWeek(requestedWeek)));
		logger.debug("Week overview: project assignments found for userId " + user.getUserId() + " in range " + range + ": " + weekOverview.getProjectAssignments().size());
		
		weekOverview.initCustomers();
		
		if (weekOverview.getCustomers() != null && weekOverview.getCustomers().size() > 0)
		{
			prefs = customerFoldPreferenceDAO.getPreferenceForUser(user, weekOverview.getCustomers());
		}
			
		if (prefs != null)
		{
			weekOverview.setFoldPreferences(new CustomerFoldPreferenceList(prefs));
		}
		else
		{
			weekOverview.setFoldPreferences(new CustomerFoldPreferenceList());
		}
		
		logger.debug("Week overview: customer fold preferences found for userId " + user.getUserId() + ": " + weekOverview.getFoldPreferences().size());
		
		weekOverview.setUser(user);
		
		return weekOverview;
	}
	
	

	/**
	 * Persist timesheets & comment
	 */
	public void persistTimesheet(Collection<TimesheetEntry> timesheetEntries, TimesheetComment comment)
	{
		for (TimesheetEntry entry : timesheetEntries)
		{
			if (StringUtils.isBlank(entry.getComment())
					&& (entry.getHours() == null || entry.getHours().equals(0f)))
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Deleting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
							" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours());
				}
				timesheetDAO.delete(entry);
				
			}
			else
			{
				if (logger.isDebugEnabled())
				{
					logger.debug("Persisting timesheet entry for assignment id " + entry.getEntryId().getProjectAssignment().getAssignmentId() +
							" for date " + entry.getEntryId().getEntryDate() + ", hours booked: " + entry.getHours()
							+ ", comment: " + entry.getComment()
					);
				}
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
	private void checkTimeAllottedOverruns(Collection<TimesheetEntry> timesheetEntries)
	{
		Set<ProjectAssignment>	projectAssignments = new HashSet<ProjectAssignment>();
		
		for (TimesheetEntry entry : timesheetEntries)
		{
			projectAssignments.add(entry.getEntryId().getProjectAssignment());
		}
		
		projectAssignmentService.checkForOverruns(projectAssignments);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.timesheet.service.TimesheetService#deleteTimesheetEntries(net.rrm.ehour.user.domain.User)
	 */
	public void deleteTimesheetEntries(User user)
	{
		timesheetCommentDAO.deleteCommentsForUser(user.getUserId());
		
		if (user.getProjectAssignments() != null && user.getProjectAssignments().size() > 0)
		{
			timesheetDAO.deleteTimesheetEntries(ProjectAssignmentUtil.getAssignmentIds(user.getProjectAssignments()));
		}
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

	/**
	 * @param customerFoldPreferenceDAO the customerFoldPreferenceDAO to set
	 */
	public void setCustomerFoldPreferenceDAO(CustomerFoldPreferenceDAO customerFoldPreferenceDAO)
	{
		this.customerFoldPreferenceDAO = customerFoldPreferenceDAO;
	}
}
