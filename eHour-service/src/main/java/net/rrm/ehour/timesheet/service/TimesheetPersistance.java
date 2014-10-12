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

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.activity.status.ActivityStatusService;
import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.exception.OverBudgetException;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetCommentDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.util.DomainUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TimesheetPersistance implements IPersistTimesheet, IDeleteTimesheetEntry {
    private static final Logger LOGGER = Logger.getLogger(TimesheetPersistance.class);

    private TimesheetDao timesheetDAO;
    private TimesheetCommentDao timesheetCommentDAO;
    private ActivityStatusService activityStatusService;
    private ApplicationContext context;
    private ApprovalStatusService approvalStatusService;

    @Autowired
    public TimesheetPersistance(TimesheetDao timesheetDAO,
                                TimesheetCommentDao timesheetCommentDAO,
                                ActivityStatusService activityStatusService,
                                ApplicationContext context,
                                ApprovalStatusService approvalStatusService) {
        this.timesheetDAO = timesheetDAO;
        this.timesheetCommentDAO = timesheetCommentDAO;
        this.activityStatusService = activityStatusService;
        this.context = context;
        this.approvalStatusService = approvalStatusService;
    }

    @Transactional
    public void deleteAllTimesheetDataForUser(User user) {
        timesheetCommentDAO.deleteCommentsForUser(user.getUserId());

        if (user.getActivities() != null && user.getActivities().size() > 0) {
            timesheetDAO.deleteTimesheetEntries(DomainUtil.getIdsFromDomainObjects(user.getActivities()));
        }
    }

    @Transactional
    @Override
    public List<ActivityStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries,
                                                              TimesheetComment comment,
                                                              DateRange weekRange) {
        Map<Activity, List<TimesheetEntry>> timesheetRows = getTimesheetAsRows(timesheetEntries);

        List<ActivityStatus> errorStatusses = new ArrayList<ActivityStatus>();

        for (Map.Entry<Activity, List<TimesheetEntry>> entry : timesheetRows.entrySet()) {
            try {
                getTimesheetPersister().validateAndPersist(entry.getKey(), entry.getValue(), weekRange);
            } catch (OverBudgetException e) {
                errorStatusses.add(e.getStatus());
            }
        }

        if (comment.getNewComment() == Boolean.FALSE || StringUtils.isNotBlank(comment.getComment())) {
            timesheetCommentDAO.persist(comment);
        }

        return errorStatusses;
    }

    // get an instance to ourselves for cheap new transactions
    private IPersistTimesheet getTimesheetPersister() {
        return context.getBean(IPersistTimesheet.class);
    }

    private Map<Activity, List<TimesheetEntry>> getTimesheetAsRows(Collection<TimesheetEntry> entries) {
        Map<Activity, List<TimesheetEntry>> timesheetRows = new HashMap<Activity, List<TimesheetEntry>>();

        for (TimesheetEntry timesheetEntry : entries) {
            Activity activity = timesheetEntry.getEntryId().getActivity();

            List<TimesheetEntry> activityEntries = (timesheetRows.containsKey(activity))
                    ? timesheetRows.get(activity)
                    : new ArrayList<TimesheetEntry>();

            activityEntries.add(timesheetEntry);

            timesheetRows.put(activity, activityEntries);
        }

        return timesheetRows;
    }

    @Transactional(rollbackFor = OverBudgetException.class, propagation = Propagation.REQUIRES_NEW)
    @NonAuditable
    public void validateAndPersist(Activity activity,
                                   List<TimesheetEntry> entries,
                                   DateRange weekRange) throws OverBudgetException {
        ActivityStatus beforeStatus = activityStatusService.getActivityStatus(activity);

        boolean checkAfterStatus = beforeStatus.isValid();

        try {
            persistEntries(activity, entries, weekRange, !beforeStatus.isValid());
        } catch (OverBudgetException obe) {
            // make sure it's retrown by checking the after status
            checkAfterStatus = true;
        }

        ActivityStatus afterStatus = activityStatusService.getActivityStatus(activity);

        if (checkAfterStatus && !afterStatus.isValid()) {
            throw new OverBudgetException(afterStatus);
        }

        updateApprovalStatusForActivityWithinWeekRange(activity, weekRange);
    }

    private void updateApprovalStatusForActivityWithinWeekRange(Activity activity, DateRange weekRange) {
        DateRange dateRangeForCurrentMonth = DateUtil.getDateRangeForMonth(weekRange.getDateStart());

        if (checkThatWeekSpansMonths(weekRange)) {
            updateApprovalStatusForWeekSpanningMonths(activity, weekRange);
        } else {
            updateApprovalStatusForMonth(activity, dateRangeForCurrentMonth);
        }
    }

    private void updateApprovalStatusForWeekSpanningMonths(Activity activity, DateRange weekRange) {
        DateRange currentMonthRange = DateUtil.getDateRangeForMonth(weekRange.getDateStart());
        DateRange nextMonthRange = DateUtil.getDateRangeForMonth(weekRange.getDateEnd());

        updateApprovalStatusForMonth(activity, currentMonthRange);
        updateApprovalStatusForMonth(activity, nextMonthRange);
    }

    private void updateApprovalStatusForMonth(Activity activity, DateRange monthRange) {
        List<ApprovalStatus> allApprovalStatuses = approvalStatusService.getApprovalStatusForUserWorkingForCustomer(activity.getAssignedUser(), activity.getProject().getCustomer(), monthRange);

        if (allApprovalStatuses == null || allApprovalStatuses.isEmpty()) {
            ApprovalStatus approvalStatus = new ApprovalStatus();
            approvalStatus.setCustomer(activity.getProject().getCustomer());
            approvalStatus.setUser(activity.getAssignedUser());
            approvalStatus.setStatus(ApprovalStatusType.IN_PROGRESS);
            approvalStatus.setStartDate(monthRange.getDateStart());
            approvalStatus.setEndDate(monthRange.getDateEnd());
            approvalStatusService.persist(approvalStatus);
        }

    }

    private Boolean checkThatWeekSpansMonths(DateRange weekRange) {
        Boolean doesWeekSpanMonths = false;

        DateRange dateRangeForCurrentMonth = DateUtil.getDateRangeForMonth(weekRange.getDateStart());
        DateRange dateRangeForNextMonth = DateUtil.getDateRangeForMonth(weekRange.getDateEnd());

        if(dateRangeForCurrentMonth.getDateStart().before(dateRangeForNextMonth.getDateStart())) {
            doesWeekSpanMonths = true;
        }
        return doesWeekSpanMonths;
    }

    private void persistEntries(Activity activity, List<TimesheetEntry> entries, DateRange weekRange, boolean onlyLessThanExisting) throws OverBudgetException {
        List<TimesheetEntry> previousEntries = timesheetDAO.getTimesheetEntriesInRange(activity, weekRange);

        for (TimesheetEntry entry : entries) {
            if (!entry.getEntryId().getActivity().equals(activity)) {
                LOGGER.error("Invalid entry in activity list, skipping: " + entry);
                continue;
            }

            if (entry.isEmptyEntry()) {
                deleteEntry(getEntry(previousEntries, entry));
            } else {
                persistEntry(onlyLessThanExisting, entry, getEntry(previousEntries, entry));
            }

            previousEntries.remove(entry);
        }

        removeOldEntries(previousEntries);
    }

    private void removeOldEntries(List<TimesheetEntry> previousEntries) {
        for (TimesheetEntry entry : previousEntries) {
            timesheetDAO.delete(entry);
        }
    }

    private void deleteEntry(TimesheetEntry existingEntry) {
        if (existingEntry != null) {
            timesheetDAO.delete(existingEntry);
        }
    }

    private void persistEntry(boolean bookOnlyLessHoursThanExistingHours, TimesheetEntry newEntry, TimesheetEntry existingEntry) throws OverBudgetException {
        if (bookOnlyLessHoursThanExistingHours &&
                (existingEntry == null ||
                        (newEntry.getHours().compareTo(existingEntry.getHours()) > 0))) {
            throw new OverBudgetException();
        }

        newEntry.setUpdateDate(new Date());

        if (existingEntry != null) {
            timesheetDAO.merge(newEntry);
        } else {
            timesheetDAO.persist(newEntry);
        }
    }

    private TimesheetEntry getEntry(List<TimesheetEntry> entries, TimesheetEntry entry) {
        int index = entries.indexOf(entry);

        if (index >= 0) {
            return entries.get(index);
        } else {
            return null;
        }
    }
}
