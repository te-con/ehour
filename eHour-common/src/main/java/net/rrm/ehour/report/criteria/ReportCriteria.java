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

package net.rrm.ehour.report.criteria;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.util.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Container for the report criteria, available and user selected
 */

public class ReportCriteria implements Serializable {
    private static final long serialVersionUID = 7406265452950554098L;
    private AvailableCriteria availableCriteria;
    private UserSelectedCriteria userSelectedCriteria;

    /**
     * Default constructor
     */
    public ReportCriteria() {
        this(new AvailableCriteria(), new UserSelectedCriteria());
    }

    public ReportCriteria(AvailableCriteria availableCriteria) {
        this(availableCriteria, new UserSelectedCriteria());
    }

    public ReportCriteria(UserSelectedCriteria userSelectedCriteria) {
        this(new AvailableCriteria(), userSelectedCriteria);
    }

    public ReportCriteria(AvailableCriteria availableCriteria, UserSelectedCriteria userSelectedCriteria) {
        this.availableCriteria = availableCriteria;
        this.userSelectedCriteria = userSelectedCriteria;
    }

    /**
     * Get report range, use the available criteria if the user didn't supply any (yet)
     *
     * @return
     */
    // TODO reduce complexity of method
    public DateRange getReportRange() {
        DateRange reportRange;

        reportRange = userSelectedCriteria.getReportRange();

        if (reportRange.getDateStart() == null || userSelectedCriteria.isInfiniteStartDate()) {
            if (availableCriteria == null || availableCriteria.getReportRange() == null) {
                reportRange.setDateStart(new Date());
            } else {
                reportRange.setDateStart(availableCriteria.getReportRange().getDateStart());
            }
        }

        if (reportRange.getDateEnd() == null || userSelectedCriteria.isInfiniteEndDate()) {
            if (availableCriteria == null || availableCriteria.getReportRange() == null) {
                reportRange.setDateEnd(new Date());
            } else {
                reportRange.setDateEnd(availableCriteria.getReportRange().getDateEnd());
            }
        }


        userSelectedCriteria.setReportRange(reportRange);

        // if no timesheets were specified, use the current month as the range
        if (reportRange.isEmpty()) {
            reportRange = DateUtil.calendarToMonthRange(new GregorianCalendar());
        }

        return reportRange;
    }

    public void sort() {
//        Collections.

//        getAvailableCriteria().getCustomers()
//
//        Collections.sort((reportCriteria.getAvailableCriteria()).getCustomers(), new CustomerComparator());
//        Collections.sort((reportCriteria.getAvailableCriteria()).getProjects(), new ProjectComparator());

    }

    /**
     * @return the userSelectedCriteria
     */
    public UserSelectedCriteria getUserSelectedCriteria() {
        return userSelectedCriteria;
    }

    /**
     * @return the availableCriteria
     */
    public AvailableCriteria getAvailableCriteria() {
        return availableCriteria;
    }
}