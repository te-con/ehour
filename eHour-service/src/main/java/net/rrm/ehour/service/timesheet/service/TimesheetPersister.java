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

package net.rrm.ehour.service.timesheet.service;

import java.util.List;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.service.exception.BusinessException;
import net.rrm.ehour.service.exception.OverBudgetException;

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
	public void validateAndPersist(ProjectAssignment assignment, 
									List<TimesheetEntry> entries,
									DateRange weekRange) throws OverBudgetException;

}
