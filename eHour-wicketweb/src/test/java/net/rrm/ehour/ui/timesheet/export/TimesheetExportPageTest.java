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

package net.rrm.ehour.ui.timesheet.export;

import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.ProjectObjectMother;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.report.detailed.DetailedReportDataObjectMother;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimesheetExportPageTest extends BaseSpringWebAppTester
{
    @Mock
	private IOverviewTimesheet overviewTimesheet;
    @Mock
	private ReportCriteriaService reportCriteriaService;
    @Mock
	private DetailedReportService detailedReportService;

	private ReportCriteria reportCriteria;
    @Mock
    private ConfigurationService configurationService;

	@Before
	public void before() throws Exception {
        getMockContext().putBean("configurationService", configurationService);
        getMockContext().putBean(overviewTimesheet);
        getMockContext().putBean("reportCriteriaService", reportCriteriaService);
        getMockContext().putBean("detailedReportService", detailedReportService);

        reportCriteria = createReportCriteria();

        when(overviewTimesheet.getBookedDaysMonthOverview(any(Integer.class), any(Calendar.class))).thenReturn(new ArrayList<LocalDate>());

        when(reportCriteriaService.syncUserReportCriteria(any(ReportCriteria.class), any(ReportCriteriaUpdateType.class)))
                .thenReturn(reportCriteria);

        when(detailedReportService.getDetailedReportData(any(ReportCriteria.class)))
                .thenReturn(DetailedReportDataObjectMother.getFlatReportData());

        tester.startPage(TimesheetExportPage.class);

        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Test
	public void submit()
	{
        FormTester formTester = tester.newFormTester("printSelectionFrame:printSelectionFrame_body:blueBorder:blueBorder_body:selectionForm:criteriaForm");
		formTester.selectMultiple("billableProjectGroup", new int[]{0, 2});
		formTester.setValue("signOff", "true");

		formTester.submit("store");

		tester.assertNoErrorMessage();
		assertEquals(Boolean.TRUE, reportCriteria.getUserSelectedCriteria().getCustomParameters().get(TimesheetExportParameter.INCL_SIGN_OFF.name()));

		assertEquals(2, reportCriteria.getUserSelectedCriteria().getProjects().size());

		Integer id =  reportCriteria.getUserSelectedCriteria().getProjects().get(1).getProjectId();

		// order is unknown
		if (id != 0 && id != 2)
		{
			fail("id should be 0 or 2");
		}
	}

	private ReportCriteria createReportCriteria()
	{
		AvailableCriteria availableCriteria = new AvailableCriteria();
		availableCriteria.setProjects(ProjectObjectMother.createProjects(5));

		return new ReportCriteria(availableCriteria);
	}
}
