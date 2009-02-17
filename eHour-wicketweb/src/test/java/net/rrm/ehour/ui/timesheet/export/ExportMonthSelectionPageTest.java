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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.DummyWebDataGenerator;
import net.rrm.ehour.ui.timesheet.export.print.PrintMonth;
import net.rrm.ehour.util.DateUtil;

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
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);

		reportCriteriaService = createMock(ReportCriteriaService.class);
		mockContext.putBean("reportCriteriaService", reportCriteriaService);
	
	}
	
	@Test
	public void testSubmitPrint()
	{
		DetailedReportService detailedReportService = createMock(DetailedReportService.class);
		mockContext.putBean("detailedReportService", detailedReportService);
		
		DateRange range = DateUtil.getDateRangeForMonth(Calendar.getInstance());
		
		Set<ProjectAssignment> assignments = new HashSet<ProjectAssignment>();
		assignments.add(DummyWebDataGenerator.getProjectAssignment(1));
		assignments.add(DummyWebDataGenerator.getProjectAssignment(2));
		assignments.add(DummyWebDataGenerator.getProjectAssignment(3));
		
		expect(timesheetService.getBookedDaysMonthOverview(isA(Integer.class),  isA(Calendar.class)))
				.andReturn(new ArrayList<BookedDay>());	
		
		ReportCriteria criteria = new ReportCriteria();
		AvailableCriteria availableCriteria = new AvailableCriteria();
		availableCriteria.setProjects(DummyWebDataGenerator.getProjects(5));
		criteria.setAvailableCriteria(availableCriteria);
		
		expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), isA(ReportCriteriaUpdateType.class)))
				.andReturn(criteria);
		
		replay(timesheetService, reportCriteriaService);
		
		tester.startPage(ExportMonthSelectionPage.class);
		
		FormTester formTester = tester.newFormTester("printSelectionFrame:blueBorder:selectionForm:criteriaForm");
		formTester.selectMultiple("projectGroup", new int[]{0, 2});
		formTester.setValue("signOff", "true");

		formTester.submit("printButton");
		
		tester.assertRenderedPage(PrintMonth.class);
		tester.assertNoErrorMessage();
		
		verify(timesheetService);
		verify(reportCriteriaService);
	}
}
