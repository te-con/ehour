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

import net.rrm.ehour.data.DateRange;
<<<<<<< HEAD:eHour-service/src/main/java/net/rrm/ehour/timesheet/service/IPersistTimesheet.java
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
=======
import net.rrm.ehour.domain.Activity;
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure:eHour-service/src/main/java/net/rrm/ehour/timesheet/service/TimesheetPersister.java
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.exception.OverBudgetException;
<<<<<<< HEAD:eHour-service/src/main/java/net/rrm/ehour/timesheet/service/IPersistTimesheet.java
import net.rrm.ehour.project.status.ProjectAssignmentStatus;

import java.util.Collection;
import java.util.List;

/**
 * Timesheet persister & validator
 */

public interface IPersistTimesheet {
    /**
     * Persist timesheet entries and comment
     *
     * @param timesheetEntries
     * @param timesheetComment
     * @param weekRange
     */
    List<ProjectAssignmentStatus> persistTimesheetWeek(Collection<TimesheetEntry> timesheetEntries,
                                                       TimesheetComment timesheetComment,
                                                       DateRange weekRange);

    void validateAndPersist(ProjectAssignment assignment,
                            List<TimesheetEntry> entries,
                            DateRange weekRange) throws OverBudgetException;

}
=======

/**
 * Timesheet persister & validator
 **/

public interface TimesheetPersister
{
	/**
	 * Validate, persist and notify list of timesheet entries
	 * @param assignment
	 * @param entries
	 * @throws BusinessException
	 */
	public void validateAndPersist(Activity activity, 
									List<TimesheetEntry> entries,
									DateRange weekRange) throws OverBudgetException;

}
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure:eHour-service/src/main/java/net/rrm/ehour/timesheet/service/TimesheetPersister.java
