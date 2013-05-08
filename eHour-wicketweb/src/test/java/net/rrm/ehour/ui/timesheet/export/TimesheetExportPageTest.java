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
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * Created on Feb 3, 2009, 11:10:24 PM
 * @author Thies Edeling (thies@te-con.nl)
 *
 */
public class TimesheetExportPageTest extends AbstractSpringWebAppTester
{
	private TimesheetService 	timesheetService;
	private ReportCriteriaService reportCriteriaService;
	private DetailedReportService detailedReportService;
	private ReportCriteria reportCriteria;
    private ConfigurationService configurationService;

	@Before
	public void before() throws Exception {
        configurationService = createMock(ConfigurationService.class);
        getMockContext().putBean("configurationService", configurationService);

        timesheetService = createMock(TimesheetService.class);
        getMockContext().putBean("timesheetService", timesheetService);

        reportCriteriaService = createMock(ReportCriteriaService.class);
        getMockContext().putBean("reportCriteriaService", reportCriteriaService);

        detailedReportService = createMock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);

        reportCriteria = createReportCriteria();

        expect(timesheetService.getBookedDaysMonthOverview(isA(Integer.class), isA(Calendar.class))).andReturn(new ArrayList<LocalDate>());

        expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), isA(ReportCriteriaUpdateType.class)))
                .andReturn(reportCriteria);

        expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
                .andReturn(DetailedReportDataObjectMother.getFlatReportData());
        replay(timesheetService, reportCriteriaService, detailedReportService);

        tester.startPage(TimesheetExportPage.class);
    }

    @Test
	public void submit()
	{
        FormTester formTester = tester.newFormTester("printSelectionFrame:printSelectionFrame_body:blueBorder:blueBorder_body:selectionForm:criteriaForm");
		formTester.selectMultiple("projectGroup", new int[]{0, 2});
		formTester.setValue("signOff", "true");

		formTester.submit("store");

		tester.assertNoErrorMessage();
		assertEquals(Boolean.TRUE, reportCriteria.getUserCriteria().getCustomParameters().get(TimesheetExportParameter.INCL_SIGN_OFF.name()));

		assertEquals(2, reportCriteria.getUserCriteria().getProjects().size());

		Integer id =  reportCriteria.getUserCriteria().getProjects().get(1).getProjectId();

		// order is unknown
		if (id != 0 && id != 2)
		{
			fail("id should be 0 or 2");
		}
		verifyMocks();
	}

	// don't put these in the teardown (@After) as failed expectations will hide any earlier thrown exceptions
	public void verifyMocks()
	{
		verify(timesheetService);
		verify(reportCriteriaService);
		verify(detailedReportService);

	}

	private ReportCriteria createReportCriteria()
	{
		AvailableCriteria availableCriteria = new AvailableCriteria();
		availableCriteria.setProjects(ProjectObjectMother.createProjects(5));

		return new ReportCriteria(availableCriteria);
	}
}
