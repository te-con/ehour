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

package net.rrm.ehour.ui.timesheet.panel;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.CustomerFoldPreference;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.CustomerFoldPreferenceList;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.DummyWebDataGenerator;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimesheetPanelTest extends AbstractSpringWebAppTester
{
	private final static String TIMESHEET_PATH = "panel:timesheetFrame:timesheetForm";
	
	private TimesheetService	timesheetService;
	private UserService			userService;
	private User				user;
	private Calendar			cal;
	
	@Before
	public void setup()
	{
		getConfig().setCompleteDayHours(8l);

		timesheetService = createMock(TimesheetService.class);
		getMockContext().putBean("timesheetService", timesheetService);

		userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);
		
		user = new User(1);
		cal = new GregorianCalendar();
		WeekOverview overview = new WeekOverview();
		overview.setUser(new User(1));
		
		Calendar now = GregorianCalendar.getInstance();
		now.add(Calendar.DAY_OF_WEEK, 7);
		
		overview.setWeekRange(new DateRange(new Date(), now.getTime()));
		
		TimesheetEntry entry = DummyWebDataGenerator.getTimesheetEntry(1, new Date(), 5);
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		entries.add(entry);
		overview.setTimesheetEntries(entries);

		List<ProjectAssignment> ass = new ArrayList<ProjectAssignment>();
		ass.add(DummyWebDataGenerator.getProjectAssignment(1));
		overview.setProjectAssignments(ass);
		
		overview.setFoldPreferences(new CustomerFoldPreferenceList());
	
		expect(timesheetService.getWeekOverview(isA(User.class), isA(Calendar.class), isA(EhourConfig.class)))
				.andReturn(overview);			
	}
	
	@Test
	public void addDayComment()
	{
		startAndReplay();
		
		final String comment = "commentaar";
	
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");

		FormTester timesheetFormTester = getTester().newFormTester(TIMESHEET_PATH);
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:submit", "onclick");
		
		Timesheet timesheet = (Timesheet)getTester().getComponentFromLastRenderedPage("panel").getModelObject();
		assertEquals(comment, timesheet.getTimesheetEntries().get(0).getComment());
		
		getTester().assertNoErrorMessage();
	}
	
	@Test
	public void addDayCommentCancelled()
	{
		startAndReplay();
		
		final String comment = "commentaar";
	
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");

		FormTester timesheetFormTester = getTester().newFormTester(TIMESHEET_PATH);
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:cancel", "onclick");
		
		Timesheet timesheet = (Timesheet)getTester().getComponentFromLastRenderedPage("panel").getModelObject();
		assertNull(timesheet.getTimesheetEntries().get(0).getComment());

		getTester().assertNoErrorMessage();
	}	
		
	
	@Test
	public void shouldBookAllHours()
	{
		startAndReplay();
		
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:bookWholeWeek", "onclick");
		getTester().assertNoErrorMessage();

		Label grandTotalLabel = (Label)getTester().getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:grandTotal");
		assertEquals("40.00", grandTotalLabel.getModelObject().toString());
		
		getTester().assertNoErrorMessage();
	}
	
	@Test
	public void updateCounts()
	{
		startAndReplay();
		
		FormTester timesheetFormTester = getTester().newFormTester(TIMESHEET_PATH);
//		more than 24 hour can be booked now so this error message doesn't popup anymore
//		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "36");
//		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
//		getTester().assertErrorMessages(new String[]{"day.DoubleRangeWithNullValidator"});
//
//		webapp.getSession().cleanupFeedbackMessages();
		
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "12");
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
		getTester().assertNoErrorMessage();
		getTester().assertContains("536e87"); // FormHighlighter -> colormodifier

		Label grandTotalLabel = (Label)getTester().getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:grandTotal");
		assertEquals("12.00", grandTotalLabel.getModelObject().toString());
	}
	
	@Test
	public void moveToNextWeek()
	{
		startAndReplay();
	
		getTester().executeAjaxEvent("panel:timesheetFrame:greyFrame:title:nextWeek", "onclick");

		Calendar cal = getWebApp().getSession().getNavCalendar();
		
		Calendar now = GregorianCalendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 1);
		
		assertTrue(now.getTime().before(cal.getTime()));
	}
	
	@Test
	public void foldCustomerRow()
	{
		userService.persistCustomerFoldPreference(isA(CustomerFoldPreference.class));
		
		startAndReplay();

		getTester().executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:foldLink", "onclick");
	}
	
//	@Test
//	public void tooManyHoursFailure()
//	{
//		startAndReplay();
//		
//		FormTester timesheetFormTester = getTester().newFormTester(TIMESHEET_PATH + "");
//		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "36");
//
//		getTester().executeAjaxEvent(TIMESHEET_PATH + ":commentsFrame:submitButton", "onclick");
//		getTester().assertErrorMessages(new String[]{"day.DoubleRangeWithNullValidator"});
//	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldSubmitSuccessful()
	{
		expect(timesheetService.persistTimesheetWeek(isA(Collection.class), isA(TimesheetComment.class), isA(DateRange.class)))
			.andReturn(new ArrayList<ProjectAssignmentStatus>());
		
		startAndReplay();
		
		getTester().executeAjaxEvent(TIMESHEET_PATH + ":commentsFrame:submitButton", "onclick");
		
		getTester().assertNoErrorMessage();
	}
	
	@After
	public void verifyMocks()
	{
		verify(timesheetService);
		verify(userService);
		
	}
	
	/**
	 * 
	 */
	private void startAndReplay()
	{
		replay(timesheetService);
		replay(userService);

		getTester().startPanel(new TestPanelSource()
		{
			private static final long serialVersionUID = -8296677055637030118L;

			public Panel getTestPanel(String panelId)
			{
				return new TimesheetPanel(panelId, user, cal);
			}
		});
	}
}
