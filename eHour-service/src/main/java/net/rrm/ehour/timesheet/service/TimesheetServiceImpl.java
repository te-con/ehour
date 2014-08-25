<<<<<<< HEAD
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

import com.google.common.collect.Lists;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.util.DateUtil;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.collection.Seq;

import java.util.*;

/**
 * Provides services for displaying and manipulating timesheets.
 * Methods are organized by their functionality rather than technical impact.
 *
 * @author Thies
 */
@Service("timesheetService")
public class TimesheetServiceImpl implements IOverviewTimesheet {
    private TimesheetDao timesheetDAO;

    private TimesheetCommentDao timesheetCommentDAO;

    private TimesheetLockService timesheetLockService;

    private AggregateReportService aggregateReportService;

    private ProjectAssignmentService projectAssignmentService;

    private EhourConfig configuration;

    @Autowired
    public TimesheetServiceImpl(TimesheetDao timesheetDAO,
                                TimesheetCommentDao timesheetCommentDAO,
                                TimesheetLockService timesheetLockService,
                                AggregateReportService aggregateReportService,
                                ProjectAssignmentService projectAssignmentService,
                                EhourConfig configuration) {
        this.timesheetDAO = timesheetDAO;
        this.timesheetCommentDAO = timesheetCommentDAO;
        this.timesheetLockService = timesheetLockService;
        this.aggregateReportService = aggregateReportService;
        this.projectAssignmentService = projectAssignmentService;
        this.configuration = configuration;
    }

    /**
     * Fetch the timesheet overview for a user. This returns an object containing the project assignments for the
     * requested month and a list with all timesheet entries for that month.
     *
     * @param userId
     * @param requestedMonth only the month and year of the calendar is used
     * @return TimesheetOverviewAction
     * @throws ObjectNotFoundException
     */
    public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth) {
        TimesheetOverview overview = new TimesheetOverview();

        DateRange monthRange = DateUtil.calendarToMonthRange(requestedMonth);
        overview.setProjectStatus(getProjectStatus(user.getUserId(), monthRange));

        List<TimesheetEntry> timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), monthRange);

        Map<Integer, List<TimesheetEntry>> calendarMap = entriesToCalendarMap(timesheetEntries);
        overview.setTimesheetEntries(calendarMap);

        return overview;
    }

    /**
     * Get project status for user
     *
     * @param userId
     * @param monthRange
     * @return
     */
    private SortedSet<UserProjectStatus> getProjectStatus(Integer userId, DateRange monthRange) {
        List<Integer> assignmentIds = Lists.newArrayList();
        SortedSet<UserProjectStatus> userProjectStatus = new TreeSet<UserProjectStatus>();
        Map<Integer, AssignmentAggregateReportElement> originalAggregates = new HashMap<Integer, AssignmentAggregateReportElement>();

        List<AssignmentAggregateReportElement> aggregates = aggregateReportService.getHoursPerAssignmentInRange(userId, monthRange);

        // only flex & fixed needed, others can already be added to the returned list
        for (AssignmentAggregateReportElement aggregate : aggregates) {
            if (aggregate.getProjectAssignment().getAssignmentType().isFixedAllottedType() ||
                    aggregate.getProjectAssignment().getAssignmentType().isFlexAllottedType()) {
                Integer assignmentId = aggregate.getProjectAssignment().getAssignmentId();
                assignmentIds.add(assignmentId);
                originalAggregates.put(assignmentId, aggregate);
            } else {
                userProjectStatus.add(new UserProjectStatus(aggregate));
            }
        }

        // fetch total hours for flex/fixed assignments
        if (assignmentIds.size() > 0) {
            List<AssignmentAggregateReportElement> timeAllottedAggregates = aggregateReportService.getHoursPerAssignment(assignmentIds);

            for (AssignmentAggregateReportElement aggregate : timeAllottedAggregates) {
                userProjectStatus.add(new UserProjectStatus(originalAggregates.get(aggregate.getProjectAssignment().getAssignmentId()),
                        aggregate.getHours()));
            }
        }

        return userProjectStatus;
    }

    /**
     * Get a list with all day numbers in this month that has complete booked days (config defines the completion
     * level).
     *
     * @param userId
     * @param requestedMonth
     * @return List with Integers of complete booked days
     */
    public List<LocalDate> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth) {
        DateRange monthRange = DateUtil.calendarToMonthRange(requestedMonth);

        List<BookedDay> bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);
        List<LocalDate> fullyBookedDays = new ArrayList<LocalDate>();

        for (BookedDay bookedDay : bookedDays) {
            if (bookedDay.getHours() != null &&
                    bookedDay.getHours().floatValue() >= configuration.getCompleteDayHours()) {
                fullyBookedDays.add(new LocalDate(bookedDay.getDate()));
            }
        }

        Collections.sort(fullyBookedDays);

        return fullyBookedDays;
    }

    /**
     * Put the timesheet entries in a map where the day is the key and
     * the value is a list of timesheet entries filled out for that date
     *
     * @param timesheetEntries
     * @return
     */
    private Map<Integer, List<TimesheetEntry>> entriesToCalendarMap(List<TimesheetEntry> timesheetEntries) {
        Map<Integer, List<TimesheetEntry>> calendarMap;
        Calendar cal;
        Integer dayKey;
        List<TimesheetEntry> dayEntries;

        calendarMap = new HashMap<Integer, List<TimesheetEntry>>();

        for (TimesheetEntry entry : timesheetEntries) {
            if (entry == null) {
                continue;
            }

            cal = new GregorianCalendar();
            cal.setTime(entry.getEntryId().getEntryDate());

            dayKey = cal.get(Calendar.DAY_OF_MONTH);

            if (calendarMap.containsKey(dayKey)) {
                dayEntries = calendarMap.get(dayKey);
            } else {
                dayEntries = new ArrayList<TimesheetEntry>();
            }

            dayEntries.add(entry);

            calendarMap.put(dayKey, dayEntries);
        }

        return calendarMap;
    }

    /**
     * Get week overview for a date. Week number of supplied requested week is used
     */
    public WeekOverview getWeekOverview(User user, Calendar requestedWeek) {
        Calendar reqWeek = (Calendar) requestedWeek.clone();
        reqWeek.setFirstDayOfWeek(configuration.getFirstDayOfWeek());

        DateRange range = DateUtil.getDateRangeForWeek(reqWeek);

        List<TimesheetEntry> timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), range);
        TimesheetComment comment = timesheetCommentDAO.findById(new TimesheetCommentId(user.getUserId(), range.getDateStart()));
        List<ProjectAssignment> assignments = projectAssignmentService.getProjectAssignmentsForUser(user.getUserId(), range);

        Seq<Interval> lockedDatesAsIntervals = timesheetLockService.findLockedDatesInRange(range.getDateStart(), range.getDateEnd(), user);
        List<Date> lockedDates = TimesheetLockService$.MODULE$.intervalToJavaList(lockedDatesAsIntervals);

        return new WeekOverview(timesheetEntries, comment, assignments, range, user, lockedDates);
    }
}
=======
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

import net.rrm.ehour.activity.service.ActivityService;
import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
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
 * Provides services for displaying and manipulating timesheets. Methods are
 * organized by their functionality rather than technical impact.
 * 
 * @author Thies
 * 
 */
@Service("timesheetService")
public class TimesheetServiceImpl implements TimesheetService {
	@Autowired
	private TimesheetDao timesheetDAO;

	@Autowired
	private TimesheetCommentDao timesheetCommentDAO;

	@Autowired
	private AggregateReportService aggregateReportService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private EhourConfig configuration;

	@Autowired
	private TimesheetPersister timesheetPersister;

	private static final Logger LOGGER = Logger.getLogger(TimesheetServiceImpl.class);

	/**
	 * Fetch the timesheet overview for a user. This returns an object
	 * containing the project assignments for the requested month and a list
	 * with all timesheet entries for that month.
	 * 
	 * @param userId
	 * @param requestedMonth
	 *            only the month and year of the calendar is used
	 * @return TimesheetOverviewAction
	 * @throws ObjectNotFoundException
	 */
	public TimesheetOverview getTimesheetOverview(User user, Calendar requestedMonth) {
		TimesheetOverview overview = new TimesheetOverview();

		DateRange monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		LOGGER.debug("Getting timesheet overview for userId " + user.getUserId() + " in range " + monthRange);

		overview.setProjectStatus(getProjectStatus(user.getUserId(), monthRange));

		List<TimesheetEntry> timesheetEntries = timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), monthRange);
		LOGGER.debug("Timesheet entries found for userId " + user.getUserId() + " in range " + monthRange + ": " + timesheetEntries.size());

		Map<Integer, List<TimesheetEntry>> calendarMap = entriesToCalendarMap(timesheetEntries);
		overview.setTimesheetEntries(calendarMap);

		return overview;
	}

	/**
	 * Get project status for user
	 * 
	 * @param userId
	 * @param monthRange
	 * @return
	 */
	private SortedSet<UserProjectStatus> getProjectStatus(Integer userId, DateRange monthRange) {
		List<ActivityAggregateReportElement> aggregates;
		List<Serializable> activityIds = new ArrayList<Serializable>();
		SortedSet<UserProjectStatus> userProjectStatus = new TreeSet<UserProjectStatus>();
		List<ActivityAggregateReportElement> timeAllottedAggregates;
		Map<Integer, ActivityAggregateReportElement> originalAggregates = new HashMap<Integer, ActivityAggregateReportElement>();
		Integer activityId;

		aggregates = aggregateReportService.getHoursPerActivityInRange(userId, monthRange);

		LOGGER.debug("Getting project status for " + aggregates.size() + " assignments");

		// only flex & fixed needed, others can already be added to the returned
		// list
		for (ActivityAggregateReportElement aggregate : aggregates) {
			activityId = aggregate.getActivity().getId();
			activityIds.add(activityId);
			originalAggregates.put(activityId, aggregate);

			// if
			// (aggregate.getProjectAssignment().getAssignmentType().isFixedAllottedType()
			// ||
			// aggregate.getProjectAssignment().getAssignmentType().isFlexAllottedType())
			// {
			// assignmentId =
			// aggregate.getProjectAssignment().getAssignmentId();
			// assignmentIds.add(assignmentId);
			// originalAggregates.put(assignmentId, aggregate);
			//
			// LOGGER.debug("Fetching total hours for assignment Id: " +
			// assignmentId);
			// }
			// else
			// {
			// userProjectStatus.add(new UserProjectStatus(aggregate));
			// }
		}

		// fetch total hours for flex/fixed assignments
		if (activityIds.size() > 0) {
			timeAllottedAggregates = aggregateReportService.getHoursPerActivity(activityIds);

			for (ActivityAggregateReportElement aggregate : timeAllottedAggregates) {
				userProjectStatus.add(new UserProjectStatus(originalAggregates.get(aggregate.getActivity().getId()), aggregate.getHours()));
			}
		}

		return userProjectStatus;
	}

	/**
	 * Get a list with all day numbers in this month that has complete booked
	 * days (config defines the completion level).
	 * 
	 * @param userId
	 * @param requestedMonth
	 * @return List with Integers of complete booked days
	 * @throws ObjectNotFoundException
	 */
	public List<BookedDay> getBookedDaysMonthOverview(Integer userId, Calendar requestedMonth) {
		DateRange monthRange;
		List<BookedDay> bookedDays;
		List<BookedDay> bookedDaysReturn = new ArrayList<BookedDay>();

		monthRange = DateUtil.calendarToMonthRange(requestedMonth);
		LOGGER.debug("Getting booked days overview for userId " + userId + " in range " + monthRange);

		bookedDays = timesheetDAO.getBookedHoursperDayInRange(userId, monthRange);

		for (BookedDay bookedDay : bookedDays) {
			if (bookedDay.getHours() != null && bookedDay.getHours().floatValue() >= configuration.getCompleteDayHours()) {
				bookedDaysReturn.add(bookedDay);
			}
		}

		LOGGER.debug("Booked days found for userId " + userId + ": " + bookedDaysReturn.size());
		LOGGER.debug("Total booked days found for userId " + userId + ": " + bookedDays.size());

		Collections.sort(bookedDaysReturn, new BookedDayComparator());

		return bookedDaysReturn;
	}

	/**
	 * Put the timesheet entries in a map where the day is the key and the value
	 * is a list of timesheet entries filled out for that date
	 * 
	 * @param timesheetEntries
	 * @return
	 */
	private Map<Integer, List<TimesheetEntry>> entriesToCalendarMap(List<TimesheetEntry> timesheetEntries) {
		Map<Integer, List<TimesheetEntry>> calendarMap;
		Calendar cal;
		Integer dayKey;
		List<TimesheetEntry> dayEntries;

		calendarMap = new HashMap<Integer, List<TimesheetEntry>>();

		for (TimesheetEntry entry : timesheetEntries) {
			if (entry == null) {
				continue;
			}

			cal = new GregorianCalendar();
			cal.setTime(entry.getEntryId().getEntryDate());

			dayKey = Integer.valueOf(cal.get(Calendar.DAY_OF_MONTH));

			if (calendarMap.containsKey(dayKey)) {
				dayEntries = calendarMap.get(dayKey);
			} else {
				dayEntries = new ArrayList<TimesheetEntry>();
			}

			dayEntries.add(entry);

			calendarMap.put(dayKey, dayEntries);
		}

		return calendarMap;
	}

	/**
	 * Get week overview for a date. Week number of supplied requested week is
	 * used
	 * 
	 * @param userId
	 * @param requestedWeek
	 * @return
	 */
	public WeekOverview getWeekOverview(User user, Calendar requestedWeek, EhourConfig config) {
		WeekOverview weekOverview;
		DateRange range;

		weekOverview = new WeekOverview();

		requestedWeek.setFirstDayOfWeek(config.getFirstDayOfWeek());
		range = DateUtil.getDateRangeForWeek(requestedWeek);

		weekOverview.setWeekRange(range);

		weekOverview.setTimesheetEntries(timesheetDAO.getTimesheetEntriesInRange(user.getUserId(), range));
		LOGGER.debug("Week overview: timesheet entries found for userId " + user.getUserId() + ": "
				+ weekOverview.getTimesheetEntries().size());

		weekOverview.setComment(timesheetCommentDAO.findById(new TimesheetCommentId(user.getUserId(), range.getDateStart())));
		LOGGER.debug("Week overview: comments found for userId " + user.getUserId() + ": " + (weekOverview.getComment() != null));

		weekOverview.setActivities(activityService.getActivitiesForUser(user.getUserId(), range));
		LOGGER.debug("Week overview: Activities found for userId " + user.getUserId() + " in range " + range + ": "
				+ weekOverview.getActivities().size());

		weekOverview.initCustomers();

		weekOverview.setUser(user);

		return weekOverview;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rrm.ehour.persistence.persistence.timesheet.service.TimesheetService
	 * #persistTimesheetWeek(java.util.Collection,
	 * net.rrm.ehour.persistence.persistence.domain.TimesheetComment,
	 * net.rrm.ehour.persistence.persistence.data.DateRange)
	 */
	@Transactional
	public List<ActivityStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries, TimesheetComment comment,
			DateRange weekRange) {
		Map<Activity, List<TimesheetEntry>> timesheetRows = getTimesheetAsRows(timesheetEntries);

		List<ActivityStatus> errorStatusses = new ArrayList<ActivityStatus>();

		for (Map.Entry<Activity, List<TimesheetEntry>> entry : timesheetRows.entrySet()) {
			try {
				timesheetPersister.validateAndPersist(entry.getKey(), entry.getValue(), weekRange);
			} catch (OverBudgetException e) {
				// TODO-NK Need to work along with OverBudgetException work to
				// correct this as well.
				// errorStatusses.add( e.getStatus());
			}
		}

		if (comment.getNewComment() == Boolean.FALSE || !StringUtils.isBlank(comment.getComment())) {
			LOGGER.debug("Persisting timesheet comment for week " + comment.getCommentId().getCommentDate());
			timesheetCommentDAO.persist(comment);
		}

		return errorStatusses;
	}

	/**
	 * Get timesheet as rows
	 * 
	 * @param entries
	 * @return
	 */
	private Map<Activity, List<TimesheetEntry>> getTimesheetAsRows(Collection<TimesheetEntry> entries) {
		// Map<ProjectAssignment, List<TimesheetEntry>> timesheetRows = new
		// HashMap<ProjectAssignment, List<TimesheetEntry>>();

		Map<Activity, List<TimesheetEntry>> timesheetRows = new HashMap<Activity, List<TimesheetEntry>>();
		for (TimesheetEntry timesheetEntry : entries) {
			Activity activity = timesheetEntry.getEntryId().getActivity();

			// ProjectAssignment assignment =
			// timesheetEntry.getEntryId().getProjectAssignment();

			List<TimesheetEntry> activitytEntries = (timesheetRows.containsKey(activity)) ? timesheetRows.get(activity)
					: new ArrayList<TimesheetEntry>();

			activitytEntries.add(timesheetEntry);

			timesheetRows.put(activity, activitytEntries);
		}

		return timesheetRows;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.rrm.ehour.persistence.persistence.timesheet.service.TimesheetService
	 * #deleteTimesheetEntries
	 * (net.rrm.ehour.persistence.persistence.user.domain.User)
	 */
	@Transactional
	public void deleteTimesheetEntries(User user) {
		timesheetCommentDAO.deleteCommentsForUser(user.getUserId());

		if (user.getProjectAssignments() != null && user.getProjectAssignments().size() > 0) {
			timesheetDAO.deleteTimesheetEntries(EhourUtil.getIdsFromDomainObjects(user.getProjectAssignments()));
		}
	}

	/**
	 * DAO setter (Spring)
	 * 
	 * @param dao
	 */
	public void setTimesheetDAO(TimesheetDao dao) {
		timesheetDAO = dao;
	}

	/**
	 * ReportData setter (Spring)
	 * 
	 * @param dao
	 */

	public void setReportService(AggregateReportService aggregateReportService) {
		this.aggregateReportService = aggregateReportService;
	}

	/**
	 * Setter for the config
	 * 
	 * @param config
	 */
	public void setEhourConfig(EhourConfig config) {
		this.configuration = config;
	}

	/**
	 * @param timesheetCommentDAO
	 *            the timesheetCommentDAO to set
	 */
	public void setTimesheetCommentDAO(TimesheetCommentDao timesheetCommentDAO) {
		this.timesheetCommentDAO = timesheetCommentDAO;
	}

	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	/**
	 * @param aggregateReportService
	 *            the aggregateReportService to set
	 */
	public void setAggregateReportService(AggregateReportService aggregateReportService) {
		this.aggregateReportService = aggregateReportService;
	}

	/**
	 * @param timesheetPersister
	 *            the timesheetPersister to set
	 */
	public void setTimesheetPersister(TimesheetPersister timesheetPersister) {
		this.timesheetPersister = timesheetPersister;
	}
}
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
