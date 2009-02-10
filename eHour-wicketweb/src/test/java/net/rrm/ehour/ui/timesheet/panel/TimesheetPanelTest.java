/**
 * Created on Jan 20, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.DummyDataGenerator;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.ui.timesheet.panel.TimesheetPanel;
import net.rrm.ehour.user.service.UserService;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TestPanelSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimesheetPanelTest extends BaseUIWicketTester
{
	private final static String TIMESHEET_PATH = "panel:timesheetFrame:timesheetForm";
	
	private TimesheetService	timesheetService;
	private UserService			userService;
	private User				user;
	private Calendar			cal;
	
	@Before
	public void setup()
	{
		config.setCompleteDayHours(8l);

		timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);

		userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);
		
		user = new User(1);
		cal = new GregorianCalendar();
		WeekOverview overview = new WeekOverview();
		overview.setUser(new User(1));
		
		Calendar now = GregorianCalendar.getInstance();
		now.add(Calendar.DAY_OF_WEEK, 7);
		
		overview.setWeekRange(new DateRange(new Date(), now.getTime()));
		
		TimesheetEntry entry = DummyDataGenerator.getTimesheetEntry(1, new Date(), 5);
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		entries.add(entry);
		overview.setTimesheetEntries(entries);

		List<ProjectAssignment> ass = new ArrayList<ProjectAssignment>();
		ass.add(DummyDataGenerator.getProjectAssignment(1));
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
	
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");

		FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:submit", "onclick");
		
		Timesheet timesheet = (Timesheet)tester.getComponentFromLastRenderedPage("panel").getModelObject();
		assertEquals(comment, timesheet.getTimesheetEntries().get(0).getComment());
		
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void addDayCommentCancelled()
	{
		startAndReplay();
		
		final String comment = "commentaar";
	
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");

		FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:cancel", "onclick");
		
		Timesheet timesheet = (Timesheet)tester.getComponentFromLastRenderedPage("panel").getModelObject();
		assertNull(timesheet.getTimesheetEntries().get(0).getComment());

		tester.assertNoErrorMessage();
	}	
		
	
	@Test
	public void shouldBookAllHours()
	{
		startAndReplay();
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:bookWholeWeek", "onclick");
		tester.assertNoErrorMessage();

		Label grandTotalLabel = (Label)tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:grandTotal");
		assertEquals("40.00", grandTotalLabel.getModelObject().toString());
		
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void updateCounts()
	{
		startAndReplay();
		
		FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
//		more than 24 hour can be booked now so this error message doesn't popup anymore
//		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "36");
//		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
//		tester.assertErrorMessages(new String[]{"day.DoubleRangeWithNullValidator"});
//
//		webapp.getSession().cleanupFeedbackMessages();
		
		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "12");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
		tester.assertNoErrorMessage();
		tester.assertContains("536e87"); // FormHighlighter -> colormodifier

		Label grandTotalLabel = (Label)tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:grandTotal");
		assertEquals("12.00", grandTotalLabel.getModelObject().toString());
	}
	
	@Test
	public void moveToNextWeek()
	{
		startAndReplay();
	
		tester.executeAjaxEvent("panel:timesheetFrame:greyFrame:title:nextWeek", "onclick");

		Calendar cal = webapp.getSession().getNavCalendar();
		
		Calendar now = GregorianCalendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 1);
		
		assertTrue(now.getTime().before(cal.getTime()));
	}
	
	@Test
	public void foldCustomerRow()
	{
		userService.persistCustomerFoldPreference(isA(CustomerFoldPreference.class));
		
		startAndReplay();

		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:foldLink", "onclick");
	}
	
//	@Test
//	public void tooManyHoursFailure()
//	{
//		startAndReplay();
//		
//		FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH + "");
//		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "36");
//
//		tester.executeAjaxEvent(TIMESHEET_PATH + ":commentsFrame:submitButton", "onclick");
//		tester.assertErrorMessages(new String[]{"day.DoubleRangeWithNullValidator"});
//	}
	
	@Test
	public void shouldSubmitSuccessful()
	{
		expect(timesheetService.persistTimesheetWeek(isA(Collection.class), isA(TimesheetComment.class), isA(DateRange.class)))
			.andReturn(new ArrayList<ProjectAssignmentStatus>());
		
		startAndReplay();
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":commentsFrame:submitButton", "onclick");
		
		tester.assertNoErrorMessage();
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

		tester.startPanel(new TestPanelSource()
		{
			private static final long serialVersionUID = -8296677055637030118L;

			public Panel getTestPanel(String panelId)
			{
				return new TimesheetPanel(panelId, user, cal);
			}
		});
	}
}
