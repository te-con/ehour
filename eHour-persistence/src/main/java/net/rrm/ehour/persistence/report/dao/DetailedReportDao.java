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

package net.rrm.ehour.persistence.report.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import java.io.Serializable;
import java.util.List;

public interface DetailedReportDao {

    /**
     * Get hours per day for assignments
     *
     * @param assignmentId
     * @param dateRange
     * @return
     */
    List<FlatReportElement> getHoursPerDayForAssignment(List<? extends Serializable> assignmentId, DateRange dateRange);

    /**
     * Get hours per day for users
     *
     * @param userIds
     * @param dateRange
     * @return
     */
    List<FlatReportElement> getHoursPerDayForUsers(List<? extends Serializable> userIds, DateRange dateRange);

    /**
     * Get hours per day for projects
     *
     * @param projectIds
     * @param dateRange
     * @return
     */
    List<FlatReportElement> getHoursPerDayForProjects(List<? extends Serializable> projectIds, DateRange dateRange);

    /**
     * Get hours per day for projects & users
     *
     * @param userIds
     * @param dateRange
     * @return
     */
    List<FlatReportElement> getHoursPerDayForProjectsAndUsers(List<? extends Serializable> projectIds, List<? extends Serializable> userIds, DateRange dateRange);

    /**
     * Get hours per day
     *
     * @param dateRange
     * @return
     */
    List<FlatReportElement> getHoursPerDay(DateRange dateRange);
}
