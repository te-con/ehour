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

package net.rrm.ehour.persistence.timesheet.dao;

import com.google.common.base.Optional;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.persistence.dao.AbstractGenericDaoHibernateScalaImpl;
import net.rrm.ehour.timesheet.dto.BookedDay;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository("timesheetDAO")
public class TimesheetDaoHibernateImpl
        extends AbstractGenericDaoHibernateScalaImpl<TimesheetEntry, TimesheetEntryId>
        implements TimesheetDao {
    public TimesheetDaoHibernateImpl() {
        super(TimesheetEntry.class);
    }

    @Override
    public List<TimesheetEntry> getTimesheetEntriesInRange(Integer userId, DateRange dateRange) {
        return applyConstraintsAndExecute(userId, dateRange, "Timesheet.getEntriesBetweenDateForUserId", TimesheetEntry.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TimesheetEntry> getTimesheetEntriesInRange(ProjectAssignment assignment, DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd", "assignment"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), assignment};
        String hql = "Timesheet.getEntriesBetweenDateForAssignment";

        return findByNamedQueryAndNamedParam(hql, keys, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TimesheetEntry> getTimesheetEntriesInRange(DateRange dateRange) {
        String[] keys = new String[]{"dateStart", "dateEnd"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd()};
        String hql = "Timesheet.getEntriesBetweenDate";

        return findByNamedQueryAndNamedParam(hql, keys, params);
    }

    @Override
    public List<BookedDay> getBookedHoursperDayInRange(Integer userId, DateRange dateRange) {
        return applyConstraintsAndExecute(userId, dateRange, "Timesheet.getBookedDaysInRangeForUserId", BookedDay.class);
    }

    private <T> List<T> applyConstraintsAndExecute(Integer userId, DateRange dateRange, String hql, Class<T> clazz) {
        String[] keys = new String[]{"dateStart", "dateEnd", "userId"};
        Object[] params = new Object[]{dateRange.getDateStart(), dateRange.getDateEnd(), userId};

        return findByNamedQueryAndNamedParam(hql, keys, params, Optional.<String>absent());
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public TimesheetEntry getLatestTimesheetEntryForAssignment(final Integer assignmentId) {
        Query query = getSession().getNamedQuery("Timesheet.getLatestEntryForAssignmentId");
        query.setInteger("assignmentId", assignmentId);

        List results = query.list();

        if (results.size() > 0) {
            return (TimesheetEntry) results.get(0);
        } else {
            return null;
        }
    }

    @Override
    public int deleteTimesheetEntries(List<? extends Serializable> assignmentIds) {
        Query query = getSession().getNamedQuery("Timesheet.deleteOnAssignmentIds");
        query.setParameterList("assignmentIds", assignmentIds);
        return query.executeUpdate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TimesheetEntry> getTimesheetEntriesAfter(ProjectAssignment assignment, Date date) {
        String[] keys = new String[]{"date", "assignment"};
        Object[] params = new Object[]{date, assignment};

        return findByNamedQueryAndNamedParam("Timesheet.getEntriesAfterDateForAssignment", keys, params);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TimesheetEntry> getTimesheetEntriesBefore(ProjectAssignment assignment, Date date) {
        String[] keys = new String[]{"date", "assignment"};
        Object[] params = new Object[]{date, assignment};

        return findByNamedQueryAndNamedParam("Timesheet.getEntriesBeforeDateForAssignment", keys, params);
    }
}
