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

package net.rrm.ehour.ui.report.panel.criteria;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.TimesheetLockDomain;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickPeriod;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickWeek;

import java.io.Serializable;
import java.util.Date;

/**
 * Backing bean for report criteria
 */

public class ReportCriteriaBackingBean implements Serializable {
    private static final long serialVersionUID = 4417220135092280759L;

    private ReportCriteria reportCriteria;
    private QuickWeek quickWeek;
    private QuickMonth quickMonth;
    private QuickQuarter quickQuarter;
    private TimesheetLockDomain reportForLock;

    public ReportCriteriaBackingBean(ReportCriteria reportCriteria) {
        this.reportCriteria = reportCriteria;
    }

    public QuickWeek getQuickWeek() {
        return quickWeek;
    }

    public void setQuickWeek(QuickWeek quickWeek) {
        quickQuarter = null;
        quickMonth = null;
        reportForLock = null;

        this.quickWeek = quickWeek;

        setReportRangeForQuickie(quickWeek);
    }

    private void setReportRangeForQuickie(QuickPeriod period) {
        if (period != null) {
            setReportRange(period.getPeriodStart(), period.getPeriodEnd());
        }
    }

    private void setReportRange(Date start, Date end) {
        UserSelectedCriteria userSelectedCriteria = reportCriteria.getUserSelectedCriteria();

        if (userSelectedCriteria.getReportRange() == null) {
            userSelectedCriteria.setReportRange(new DateRange());
        }

        userSelectedCriteria.getReportRange().setDateStart(start);
        userSelectedCriteria.getReportRange().setDateEnd(end);
    }

    public ReportCriteria getReportCriteria() {
        return reportCriteria;
    }

    public QuickMonth getQuickMonth() {
        return quickMonth;
    }

    public void setQuickMonth(QuickMonth quickMonth) {
        quickWeek = null;
        quickQuarter = null;
        reportForLock = null;
        this.quickMonth = quickMonth;
        setReportRangeForQuickie(quickMonth);
    }

    public QuickQuarter getQuickQuarter() {
        return quickQuarter;
    }

    public void setQuickQuarter(QuickQuarter quickQuarter) {
        quickWeek = null;
        quickMonth = null;
        reportForLock = null;
        this.quickQuarter = quickQuarter;
        setReportRangeForQuickie(quickQuarter);
    }

    public void resetQuickSelections() {
        quickWeek = null;
        quickMonth = null;
        quickQuarter = null;
    }

    public void setReportCriteria(ReportCriteria reportCriteria) {
        this.reportCriteria = reportCriteria;
    }

    public TimesheetLockDomain getReportForLock() {
        return reportForLock;
    }

    public void setReportForLock(TimesheetLockDomain reportForLock) {
        this.reportForLock = reportForLock;

        quickWeek = null;
        quickMonth = null;
        quickQuarter = null;

        if (reportForLock != null) {
            setReportRange(reportForLock.getDateStart(), reportForLock.getDateEnd());
        }
    }
}
