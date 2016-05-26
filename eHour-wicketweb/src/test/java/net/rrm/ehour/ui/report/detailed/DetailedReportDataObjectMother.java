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

package net.rrm.ehour.ui.report.detailed;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserSelectedCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"deprecation"})
public class DetailedReportDataObjectMother {
    private DetailedReportDataObjectMother() {
    }

    public static List<FlatReportElement> getFlatReportElements() {
        List<FlatReportElement> els = new ArrayList<>();

        {
            FlatReportElement fre = new FlatReportElement();
            fre.setAssignmentId(1);
            fre.setComment("ja hallo");
            fre.setCustomerCode("AA");
            fre.setCustomerId(1);
            fre.setCustomerName("A Company");
            fre.setDayDate(new Date(2007 - 1900, 12 - 1, 31));
            fre.setDisplayOrder(1);
            fre.setEntryDate("492007");
            fre.setTotalHours(5);
            fre.setTotalTurnOver(15);
            fre.setProjectId(1);
            fre.setProjectName("PRJ");
            fre.setProjectCode("PRJ");
            fre.setUserId(5);
            els.add(fre);
        }

        {
            FlatReportElement fre = new FlatReportElement();
            fre.setAssignmentId(1);
            fre.setComment("ja hallo");
            fre.setCustomerCode("AA");
            fre.setCustomerId(1);
            fre.setCustomerName("A Company");
            fre.setDayDate(new Date(2007 - 1900, 12 - 1, 30));
            fre.setDisplayOrder(1);
            fre.setEntryDate("492007");
            fre.setTotalHours(6);
            fre.setTotalTurnOver(16);
            fre.setProjectId(1);
            fre.setProjectName("PRJ");
            fre.setProjectCode("PRJ");

            fre.setUserId(5);
            els.add(fre);
        }

        {
            FlatReportElement fre = new FlatReportElement();
            fre.setAssignmentId(2);
            fre.setComment("ja hallo");
            fre.setCustomerCode("AA");
            fre.setCustomerId(1);
            fre.setCustomerName("A Company");
            fre.setDayDate(new Date(2007 - 1900, 12 - 1, 29));
            fre.setDisplayOrder(1);
            fre.setEntryDate("492007");
            fre.setTotalHours(7);
            fre.setTotalTurnOver(17);
            fre.setProjectId(1);
            fre.setProjectName("PRJ");
            fre.setProjectCode("PRJ");

            fre.setUserId(6);
            els.add(fre);
        }

        {
            FlatReportElement fre = new FlatReportElement();
            fre.setAssignmentId(2);
            fre.setComment("ja hallo");
            fre.setCustomerCode("AA");
            fre.setCustomerId(1);
            fre.setCustomerName("A Company");
            fre.setDayDate(new Date(2007 - 1900, 12 - 1, 28));
            fre.setDisplayOrder(2);
            fre.setEntryDate("492007");
            fre.setTotalHours(8);
            fre.setTotalTurnOver(18);
            fre.setProjectId(1);
            fre.setProjectName("PRJ");
            fre.setProjectCode("PRJ");

            fre.setUserId(6);
            els.add(fre);
        }

        {
            FlatReportElement fre = new FlatReportElement();
            fre.setAssignmentId(3);
            fre.setComment("ja hallo");
            fre.setCustomerCode("AA");
            fre.setCustomerId(1);
            fre.setCustomerName("A Company");
            fre.setDayDate(new Date(2007 - 1900, 12 - 1, 28));
            fre.setDisplayOrder(2);
            fre.setEntryDate("492007");
            fre.setTotalHours(8.25);
            fre.setTotalTurnOver(18);
            fre.setProjectId(2);
            fre.setProjectName("PRJB");
            fre.setProjectCode("PRJB");

            fre.setUserId(6);
            els.add(fre);
        }
        return els;
    }

    public static ReportData getFlatReportData() {
        DateRange date = new DateRange(new Date(2007 - 1900, 12 - 1, 27), new Date(2008 - 1900, 0, 1));
        return new ReportData(DetailedReportDataObjectMother.getFlatReportElements(), date, new UserSelectedCriteria());
    }

    public static ReportCriteria getReportCriteria() {
        UserSelectedCriteria userSelectedCriteria = new UserSelectedCriteria();
        userSelectedCriteria.setReportRange(new DateRange(new Date(), new Date()));

        return new ReportCriteria(userSelectedCriteria);
    }
}
