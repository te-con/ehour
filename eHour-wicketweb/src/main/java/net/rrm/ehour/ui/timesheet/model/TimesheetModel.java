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

package net.rrm.ehour.ui.timesheet.model;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.timesheet.service.IPersistTimesheet;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebUtils;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.dto.TimesheetFactory;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Calendar;
import java.util.List;

/**
 * Model that holds the timesheet
 */
public class TimesheetModel implements PersistableTimesheetModel<TimesheetContainer> {
    private static final long serialVersionUID = 4134613450587087107L;

    @SpringBean
    private IPersistTimesheet persistTimesheet;

    @SpringBean
    private IOverviewTimesheet overviewTimesheet;

    private TimesheetContainer timesheetContainer;

    public void init(User user, Calendar forWeek) {
        WebUtils.springInjection(this);
        timesheetContainer = load(user, forWeek);
    }

    private TimesheetContainer load(User user, Calendar forWeek) {
        EhourConfig config = EhourWebSession.getEhourConfig();
        WeekOverview weekOverview = overviewTimesheet.getWeekOverview(user, forWeek);

        Timesheet timesheet = new TimesheetFactory(config, weekOverview).createTimesheet();

        if (timesheet.getComment() == null) {
            TimesheetComment comment = new TimesheetComment();
            comment.setNewComment(Boolean.TRUE);
            timesheet.setComment(comment);
        }

        return new TimesheetContainer(timesheet);
    }

    @Override
    public List<ProjectAssignmentStatus> persist() throws UnknownPersistenceException {
        if (timesheetContainer != null) {
            WebUtils.springInjection(this);
            return persistTimesheet(timesheetContainer.getTimesheet());
        } else {
            throw new UnknownPersistenceException("Failed to receive timesheet container");
        }
    }

    private List<ProjectAssignmentStatus> persistTimesheet(Timesheet timesheet) {
        WebUtils.springInjection(this);

        return persistTimesheet.persistTimesheetWeek(timesheet.getTimesheetEntries(),
                timesheet.getCommentForPersist(),
                new DateRange(timesheet.getWeekStart(), timesheet.getWeekEnd()), timesheet.getUser());
    }

    @Override
    public TimesheetContainer getObject() {
        return timesheetContainer;
    }

    @Override
    public void setObject(TimesheetContainer sheet) {
        if (sheet != null) {
            this.timesheetContainer = sheet;
        }
    }

    public void detach() {
        overviewTimesheet = null;
        persistTimesheet = null;
    }

    public static class UnknownPersistenceException extends Exception {
        public UnknownPersistenceException(String message) {
            super(message);
        }
    }
}
