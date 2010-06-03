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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;

import net.rrm.ehour.domain.ProjectMother;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.service.report.service.DetailedReportService;
import net.rrm.ehour.service.report.service.ReportCriteriaService;
import net.rrm.ehour.service.timesheet.service.TimesheetService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;


/**
 * Created on Feb 3, 2009, 11:10:24 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportMonthSelectionPageTest extends AbstractSpringWebAppTester
{
	private TimesheetService 	timesheetService;
	private ReportCriteriaService reportCriteriaService;
	private DetailedReportService detailedReportService;
	private ReportCriteria reportCriteria;
	
	@Before
	public void before() throws Exception
	{
		timesheetService = createMock(TimesheetService.class);
		getMockContext().putBean("timesheetService", timesheetService);

		reportCriteriaService = createMock(ReportCriteriaService.class);
		getMockContext().putBean("reportCriteriaService", reportCriteriaService);
	
		detailedReportService = createMock(DetailedReportService.class);
		getMockContext().putBean("detailedReportService", detailedReportService);
		
		reportCriteria = createReportCriteria();
		
		expect(timesheetService.getBookedDaysMonthOverview(isA(Integer.class),  isA(Calendar.class)))
				.andReturn(new ArrayList<BookedDay>());	
		
		expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), isA(ReportCriteriaUpdateType.class)))
				.andReturn(reportCriteria);

		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
				.andReturn(ReportTestUtil.getFlatReportData());
		replay(timesheetService, reportCriteriaService, detailedReportService);
		
		getTester().startPage(ExportMonthSelectionPage.class);		
	}
	
	@Test
	public void submitToPrint()
	{
		FormTester formTester = getTester().newFormTester("printSelectionFrame:blueBorder:selectionForm:criteriaForm");
		formTester.selectMultiple("projectGroup", new int[]{0, 2});
		formTester.setValue("signOff", "true");

		formTester.submit("printButton");
		
		getTester().assertRenderedPage(PrintMonth.class);
		getTester().assertNoErrorMessage();
		assertEquals(Boolean.TRUE, (Boolean) reportCriteria.getUserCriteria().getCustomParameters().get(ExportCriteriaParameter.INCL_SIGN_OFF.name()));
		
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
		availableCriteria.setProjects(ProjectMother.createProjects(5));
		
		ReportCriteria criteria = new ReportCriteria(availableCriteria);
		
		return criteria;
	}
}
