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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.ITestPanelSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

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
		
		TimesheetEntry entry = TimesheetEntryMother.getTimesheetEntry(1, new Date(), 5);
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		entries.add(entry);
		overview.setTimesheetEntries(entries);

		List<ProjectAssignment> ass = new ArrayList<ProjectAssignment>();
		ass.add(ProjectAssignmentMother.createProjectAssignment(1));
		overview.setProjectAssignments(ass);
		
		expect(timesheetService.getWeekOverview(isA(User.class), isA(Calendar.class), isA(EhourConfig.class)))
				.andReturn(overview);			
	}
	
	@Test
	public void addDayComment()
	{
		startAndReplay();
		
		final String comment = "commentaar";
	
		ModalWindow window = (ModalWindow)tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin");
		System.out.println(window.hashCode());
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");
		assertTrue(window.isShown());

		FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
		timesheetFormTester.setValue("blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:submit", "onclick");
		
		Timesheet timesheet = (Timesheet)tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject();
		assertEquals(comment, timesheet.getTimesheetEntries().get(0).getComment());
		
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void addDayCommentCancelled()
	{
		startAndReplay();
		
		final String comment = "commentaar";
	
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayLink", "onclick");

		FormTester formTester = tester.newFormTester(TIMESHEET_PATH);
		formTester.setValue("blueFrame:customers:0:rows:0:day1:dayWin:content:comment", comment);
		
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:comment", "onchange");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:dayWin:content:cancel", "onclick");
		
		Timesheet timesheet = (Timesheet)tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject();
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
		assertEquals(40f, (Float)grandTotalLabel.getDefaultModelObject(), 0.01f);
		
		tester.assertNoErrorMessage();
	}
	
	@Test
	public void updateCounts()
	{
		startAndReplay();
		
		FormTester formTester = tester.newFormTester(TIMESHEET_PATH);
//		more than 24 hour can be booked now so this error message doesn't popup anymore
//		setFormValue(timesheetFormTester, "blueFrame:customers:0:rows:0:day1:day", "36");
//		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
//		tester.assertErrorMessages(new String[]{"day.DoubleRangeWithNullValidator"});
//
//		webapp.getSession().cleanupFeedbackMessages();
		
		formTester.setValue("blueFrame:customers:0:rows:0:day1:day", "12");
		tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
		tester.assertNoErrorMessage();
		tester.assertContains("blueFrame:customers:0:rows:0:day1:day");

		Label grandTotalLabel = (Label)tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:grandTotal");
		assertEquals(12f, (Float)grandTotalLabel.getDefaultModelObject(), 0.01f);
	}
	
	@Test
	public void moveToNextWeek()
	{
		startAndReplay();
	
		tester.executeAjaxEvent("panel:timesheetFrame:greyFrame:title:nextWeek", "onclick");

		Calendar cal = getWebApp().getSession().getNavCalendar();
		
		Calendar now = GregorianCalendar.getInstance();
		now.add(Calendar.DAY_OF_YEAR, 1);
		
		assertTrue(now.getTime().before(cal.getTime()));
	}

    @Test
    @Ignore("The two requests are performed in isolation")
    public void shouldNotResendUnmodifiedEntries()
    {
        startAndReplay();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue("blueFrame:customers:0:rows:0:day1:day", "12");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains("blueFrame:customers:0:rows:0:day1:day");

        tester.setupRequestAndResponse();

        //changing another field should not resend the unmodified day1
        formTester.setValue("blueFrame:customers:0:rows:0:day2:day", "8");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day2:day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains("blueFrame:customers:0:rows:0:day2:day");
        tester.assertContainsNot("blueFrame:customers:0:rows:0:day1:day");
    }

    @Test
    @Ignore("The tree requests are performed in isolation")
    public void shouldResetErrorState()
    {
        startAndReplay();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue("blueFrame:customers:0:rows:0:day1:day", "12");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains("blueFrame:customers:0:rows:0:day1:day");
        tester.assertContainsNot("color: #ff0000");


        tester.setupRequestAndResponse(true);

        formTester.setValue("blueFrame:customers:0:rows:0:day1:day", "ff");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
        tester.assertContains("blueFrame:customers:0:rows:0:day1:day");
        tester.assertContains("color: #ff0000");
        tester.assertErrorMessages(new String[] { "day.IConverter.Float"});

        tester.setupRequestAndResponse(true);

        formTester.setValue("blueFrame:customers:0:rows:0:day1:day", "1");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:customers:0:rows:0:day1:day", "onblur");
        tester.assertContains("blueFrame:customers:0:rows:0:day1:day");
        tester.assertContainsNot("color: #ff0000");
    }
	

	@SuppressWarnings("unchecked")
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
	
	private void startAndReplay()
	{
		replay(timesheetService);
		replay(userService);

		tester.startPanel(new ITestPanelSource()
		{
			private static final long serialVersionUID = -8296677055637030118L;

			public Panel getTestPanel(String panelId)
			{
				return new TimesheetPanel(panelId, user, cal);
			}
		});
	}
}
