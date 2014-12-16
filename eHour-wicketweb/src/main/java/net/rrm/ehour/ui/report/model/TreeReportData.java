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

package net.rrm.ehour.ui.report.model;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;

import java.util.List;

/**
 * Created on Mar 17, 2009, 11:55:00 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class TreeReportData extends ReportData {
    private static final long serialVersionUID = -8826568609798407812L;

    private ReportData rawReportData;

    public TreeReportData(List<? extends ReportElement> reportElements, DateRange reportRange, ReportData rawReportData, UserSelectedCriteria criteria) {
        super(reportElements, reportRange, criteria);

        this.rawReportData = rawReportData;
    }

    public ReportData getRawReportData() {
        return rawReportData;
    }

}
