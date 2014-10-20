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

import com.google.common.base.Optional;
import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.OverBudgetException;

import java.util.Collection;
import java.util.List;

/**
 * Timesheet persister & validator
 */

public interface IPersistTimesheet {
    List<ActivityStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries,
                                              TimesheetComment timesheetComment,
                                              DateRange weekRange);

    List<ActivityStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries,
                                              TimesheetComment timesheetComment,
                                              DateRange weekRange,
                                              Optional<User> moderator);

    void validateAndPersist(Activity activity,
                            List<TimesheetEntry> entries,
                            DateRange weekRange,
                            Optional<User> moderator) throws OverBudgetException;

}
