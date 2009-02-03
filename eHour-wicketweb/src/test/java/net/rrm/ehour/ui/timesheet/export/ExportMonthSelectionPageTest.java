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

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
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
	private ProjectService 		projectService;
	private TimesheetService 	timesheetService;
	
	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		projectService = createMock(ProjectService.class);
		mockContext.putBean("projectService", projectService);

		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);
	}
	
	@Test
	public void testProjectAdminRender()
	{
		DateRange range = DateUtil.getDateRangeForMonth(Calendar.getInstance());
		
		Set<ProjectAssignment> assignments = new HashSet<ProjectAssignment>();
		assignments.add(DummyDataGenerator.getProjectAssignment(1));
		assignments.add(DummyDataGenerator.getProjectAssignment(2));
		assignments.add(DummyDataGenerator.getProjectAssignment(3));
		
		expect(timesheetService.getBookedDaysMonthOverview(isA(Integer.class),  isA(Calendar.class)))
				.andReturn(new ArrayList<BookedDay>());	
		
		expect(projectService.getProjectsForUser(1, range))
				.andReturn(assignments);
		
		replay(projectService, timesheetService);
		
		tester.startPage(ExportMonthSelectionPage.class);
		
//		FormTester formTester = tester.newFormTester("printSelectionFrame:blueBorder:selectionForm");
//		formTester.select("assignments:0:check", 0);
//		
		tester.assertRenderedPage(ExportMonthSelectionPage.class);
		tester.assertNoErrorMessage();
		
		verify(projectService);
		verify(timesheetService);
	
	}
}
