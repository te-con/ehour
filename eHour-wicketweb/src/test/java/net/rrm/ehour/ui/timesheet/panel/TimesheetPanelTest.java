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

<<<<<<< HEAD
package net.rrm.ehour.ui.timesheet.panel;

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
=======
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

import net.rrm.ehour.activity.status.ActivityStatus;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.ActivityMother;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentMother;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryMother;
import net.rrm.ehour.domain.User;
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.timesheet.service.IPersistTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
<<<<<<< HEAD

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

public class TimesheetPanelTest extends BaseSpringWebAppTester {
    private static final String TIMESHEET_PATH = "panel:timesheetFrame:timesheetFrame_body:timesheetForm";
    private static final String DAY1_PATH = "blueFrame:blueFrame_body:customers:0:rows:0:day1";
    private static final String DAY1_FULL_PATH = TIMESHEET_PATH + ":" + DAY1_PATH;
    private static final User USER = new User(1);

    private IPersistTimesheet persistTimesheet;
    private IOverviewTimesheet overviewTimesheet;
    private UserService userService;

    @Before
    public void setup() {
        getConfig().setCompleteDayHours(8l);
        EhourWebSession.getSession().reloadConfig();

        persistTimesheet = createMock(IPersistTimesheet.class);
        getMockContext().putBean(persistTimesheet);

        overviewTimesheet = createMock(IOverviewTimesheet.class);
        getMockContext().putBean(overviewTimesheet);

        userService = createMock(UserService.class);
        getMockContext().putBean("userService", userService);
    }

    @Test
    public void addDayComment() {
        startAndReplayWithDefaultWeekOverview();

        final String comment = "commentaar";

        ModalWindow window = openCommentWindow(DAY1_FULL_PATH);
        assertTrue(window.isShown());

        FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
        timesheetFormTester.setValue(DAY1_PATH + ":dayWin:content:comment", comment);

        tester.executeAjaxEvent(DAY1_FULL_PATH + ":dayWin:content:comment", "onchange");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":dayWin:content:submit", "onclick");

        Timesheet timesheet = (Timesheet) tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject();
        assertEquals(comment, timesheet.getTimesheetEntries().get(0).getComment());

        tester.assertNoErrorMessage();
    }

    private ModalWindow openCommentWindow(String path) {
        ModalWindow window = (ModalWindow) tester.getComponentFromLastRenderedPage(path + ":dayWin");
        tester.executeAjaxEvent(path + ":commentLink", "onclick");
        return window;
    }

    @Test
    public void addDayCommentCancelled() {
        startAndReplayWithDefaultWeekOverview();

        final String comment = "commentaar";

        clickDay1();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);
        formTester.setValue(DAY1_PATH + ":dayWin:content:comment", comment);

        tester.executeAjaxEvent(DAY1_FULL_PATH + ":dayWin:content:comment", "onchange");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":dayWin:content:cancel", "onclick");

        Timesheet timesheet = (Timesheet) tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject();
        assertNull(timesheet.getTimesheetEntries().get(0).getComment());

        tester.assertNoErrorMessage();
    }

    private void clickDay1() {
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":commentLink", "onclick");
    }

    @Test
    public void shouldBookAllHours() {
        startAndReplayWithDefaultWeekOverview();

        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:bookWholeWeek", "onclick");
        tester.assertNoErrorMessage();

        Label grandTotalLabel = (Label) tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:grandTotal");
        assertEquals(40f, (Float) grandTotalLabel.getDefaultModelObject(), 0.01f);

        tester.assertNoErrorMessage();
    }

    @Test
    public void updateCounts() {
        startAndReplayWithDefaultWeekOverview();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue(DAY1_PATH + ":day", "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains(DAY1_PATH + ":day");

        Label grandTotalLabel = (Label) tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:grandTotal");
        assertEquals(12f, (Float) grandTotalLabel.getDefaultModelObject(), 0.01f);
    }

    @Test
    public void moveToNextWeek() {
        startAndReplayWithDefaultWeekOverview();

        tester.executeAjaxEvent("panel:timesheetFrame:title:nextWeek", "onclick");

        Calendar cal = getWebApp().getSession().getNavCalendar();

        Calendar now = GregorianCalendar.getInstance();
        now.add(Calendar.DAY_OF_YEAR, 1);

        assertTrue(now.getTime().before(cal.getTime()));
    }

    @Test
    public void shouldNotResendUnmodifiedEntries() {
        startAndReplayWithDefaultWeekOverview();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue(DAY1_PATH + ":day", "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains(DAY1_PATH + ":day");

        //changing another field should not resend the unmodified day1
        formTester.setValue("blueFrame:blueFrame_body:customers:0:rows:0:day2:day", "8");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day2:day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains("blueFrame:blueFrame_body:customers:0:rows:0:day2:day");
    }

    @Test
    public void shouldResetErrorState() {
        startAndReplayWithDefaultWeekOverview();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue(DAY1_PATH + ":day", "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains(DAY1_PATH + ":day");
        tester.assertContainsNot("color: #ff0000");

        formTester.setValue(DAY1_PATH + ":day", "ff");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":day", "onblur");
        tester.assertContains(DAY1_PATH + ":day");
        tester.assertContains("color: #ff0000");
        tester.assertErrorMessages("day.IConverter.Float");

        formTester.setValue(DAY1_PATH + ":day", "1");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":day", "onblur");
        tester.assertContains(DAY1_PATH + ":day");
        tester.assertContainsNot("color: #ff0000");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void shouldSubmitSuccessful() {
        expect(persistTimesheet.persistTimesheetWeek(isA(Collection.class), isA(TimesheetComment.class), isA(DateRange.class)))
                .andReturn(new ArrayList<ProjectAssignmentStatus>());

        startAndReplayWithDefaultWeekOverview();

        tester.executeAjaxEvent(TIMESHEET_PATH + ":commentsFrame:commentsFrame_body:submitButton", "onclick");

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldDisableInputForLockedDays() {
        Date lockedDay = new LocalDate().plusDays(1).toDate();

        startAndReplayWithLockedDays(Arrays.asList(lockedDay));

        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day2:day", Label.class);
        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day3:day", TimesheetTextField.class);

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldDisableCommentInputForLockedDaysWithExistingComment() {
        // given
        Date lockedDay = new LocalDate().plusDays(1).toDate();
        List<Date> lockedDates = Arrays.asList(lockedDay);

        Calendar now = GregorianCalendar.getInstance();
        now.add(Calendar.DAY_OF_WEEK, 7);

        DateRange nextWeekRange = new DateRange(new Date(), now.getTime());

        TimesheetEntry timesheetEntry = TimesheetEntryObjectMother.createTimesheetEntry(1, lockedDay, 5);
        timesheetEntry.setComment("Comment");

        List<TimesheetEntry> entries = Arrays.asList(timesheetEntry);
        List<ProjectAssignment> assignments = Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1));

        WeekOverview overview = new WeekOverview(entries, null, assignments, nextWeekRange, USER, lockedDates);

        expectDefaultWeekOverview(overview);

        replay(overviewTimesheet);
        replay(userService);
        replay(persistTimesheet);

        // when
        tester.startComponentInPage(new TimesheetPanel("panel", USER, new GregorianCalendar()));

        // then
        openCommentWindow(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day2");

        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day2:dayWin:content:comment", Label.class);

        tester.assertNoErrorMessage();
        verify(overviewTimesheet, userService, persistTimesheet);
    }

    @Test
    public void shouldHideCommentInputLinkForLockedDaysWithoutComment() {
        Date lockedDay = new LocalDate().plusDays(1).toDate();

        startAndReplayWithLockedDays(Arrays.asList(lockedDay));

        Component commentLink = tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:day2:commentLink");
        assertNull(commentLink); // null = not visible...

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldAddLockedIconInDayForLockedDay() {
        Date lockedDay = new LocalDate().plusDays(1).toDate();

        startAndReplayWithLockedDays(Arrays.asList(lockedDay));

        String path = TIMESHEET_PATH + ":blueFrame:blueFrame_body:day2Label:lock:lockedContainer";
        tester.assertVisible(path);

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldHideBookWholeWeekIconWhenDisabledInConfig() {
        webApp.setEnableBookWholeWeek(false);

        startAndReplayWithDefaultWeekOverview();

        Component book = tester.getComponentFromLastRenderedPage("panel:timesheetFrame:timesheetFrame_body:timesheetForm:blueFrame:blueFrame_body:customers:0:rows:0:bookWholeWeek");
        // not visible = null
        assertNull(book);
    }


    @After
    public void verifyMocks() {
        verify(overviewTimesheet);
        verify(userService);
        verify(persistTimesheet);

    }

    private void startAndReplayWithDefaultWeekOverview() {
        startAndReplayWithLockedDays(Lists.<Date>newArrayList());
    }

    private void startAndReplayWithLockedDays(List<Date> lockedDays) {
        expectDefaultWeekOverview(withDefaultWeekOverview(lockedDays));

        replay(overviewTimesheet);
        replay(userService);
        replay(persistTimesheet);

        tester.startComponentInPage(new TimesheetPanel("panel", USER, new GregorianCalendar()));
    }

    private void expectDefaultWeekOverview(WeekOverview overview) {
        expect(overviewTimesheet.getWeekOverview(isA(User.class), isA(Calendar.class))).andReturn(overview);
    }

    private WeekOverview withDefaultWeekOverview(List<Date> lockedDates) {
        Calendar now = GregorianCalendar.getInstance();
        now.add(Calendar.DAY_OF_WEEK, 7);

        DateRange nextWeekRange = new DateRange(new Date(), now.getTime());

        List<TimesheetEntry> entries = Arrays.asList(TimesheetEntryObjectMother.createTimesheetEntry(1, new Date(), 5));
        List<ProjectAssignment> assignments = Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1));

        return new WeekOverview(entries, null, assignments, nextWeekRange, USER, lockedDates);
    }
}
=======

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
		
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(ActivityMother.createActivity(1));
		overview.setActivities(activities);
		
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
		tester.assertContains("536e87"); // FormHighlighter -> colormodifier

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
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldSubmitSuccessful()
	{
		expect(timesheetService.persistTimesheetWeek(isA(Collection.class), isA(TimesheetComment.class), isA(DateRange.class)))
			.andReturn(new ArrayList<ActivityStatus>());
		
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
>>>>>>> 420c91d... EHV-23, EHV-24: Modifications in Service, Dao and UI layers for Customer --> Project --> Activity structure
