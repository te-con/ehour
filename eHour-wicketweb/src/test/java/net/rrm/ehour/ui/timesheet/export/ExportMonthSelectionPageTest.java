/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.timesheet.export;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;

import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.DummyWebDataGenerator;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;


/**
 * Created on Feb 3, 2009, 11:10:24 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class ExportMonthSelectionPageTest extends BaseUIWicketTester
{
	private TimesheetService 	timesheetService;
	private ReportCriteriaService reportCriteriaService;
	private DetailedReportService detailedReportService;
	private ReportCriteria reportCriteria;
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);

		reportCriteriaService = createMock(ReportCriteriaService.class);
		mockContext.putBean("reportCriteriaService", reportCriteriaService);
	
		detailedReportService = createMock(DetailedReportService.class);
		mockContext.putBean("detailedReportService", detailedReportService);
		
		reportCriteria = createReportCriteria();
		
		expect(timesheetService.getBookedDaysMonthOverview(isA(Integer.class),  isA(Calendar.class)))
				.andReturn(new ArrayList<BookedDay>());	
		
		expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), isA(ReportCriteriaUpdateType.class)))
				.andReturn(reportCriteria);

		expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
				.andReturn(createReportData());
		replay(timesheetService, reportCriteriaService, detailedReportService);
		
		tester.startPage(ExportMonthSelectionPage.class);		
	}
	
	@Test
	public void submitToPrint()
	{
		FormTester formTester = tester.newFormTester("printSelectionFrame:blueBorder:selectionForm:criteriaForm");
		formTester.selectMultiple("projectGroup", new int[]{0, 2});
		formTester.setValue("signOff", "true");

		formTester.submit("printButton");
		
		tester.assertRenderedPage(PrintMonth.class);
		tester.assertNoErrorMessage();
		assertEquals(Boolean.TRUE, (Boolean) reportCriteria.getUserCriteria().getCustomParameters().get(ExportCriteriaParameter.INCL_SIGN_OFF.name()));
		verifyMocks();
	}

	// don't put these in the teardown (@After) as failed expectations will hide any earlier thrown exceptions
	public void verifyMocks()
	{
		verify(timesheetService);
		verify(reportCriteriaService);
		verify(detailedReportService);
		
	}
	
	private ReportData<FlatReportElement> createReportData()
	{
		ReportData<FlatReportElement> reportData = new ReportData<FlatReportElement>();
		
		return reportData;
	}
	
	private ReportCriteria createReportCriteria()
	{
		ReportCriteria criteria = new ReportCriteria();

		AvailableCriteria availableCriteria = new AvailableCriteria();
		availableCriteria.setProjects(DummyWebDataGenerator.getProjects(5));
		criteria.setAvailableCriteria(availableCriteria);
		
		return criteria;
	}
}
