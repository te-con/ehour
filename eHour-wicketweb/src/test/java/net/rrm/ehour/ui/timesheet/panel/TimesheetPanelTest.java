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

import com.google.common.collect.Lists;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.project.status.ProjectAssignmentStatus;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.timesheet.service.IPersistTimesheet;
import net.rrm.ehour.timesheet.service.TimesheetLockService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.dto.Timesheet;
import net.rrm.ehour.ui.timesheet.model.TimesheetContainer;
import net.rrm.ehour.ui.timesheet.model.TimesheetModel;
import net.rrm.ehour.ui.timesheet.panel.dailycomments.DailyCommentPanelFactory;
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactory;
import net.rrm.ehour.ui.timesheet.panel.renderer.SectionRenderFactoryCollection;
import net.rrm.ehour.ui.timesheet.panel.renderer.TimesheetIconRenderFactoryCollection;
import net.rrm.ehour.ui.timesheet.panel.weeklycomment.WeeklyCommentPanelRenderFactory;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.Component;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimesheetPanelTest extends BaseSpringWebAppTester {
    private static final String TIMESHEET_PATH = "panel:timesheetFrame:timesheetFrame_body:timesheetForm";
    private static final String DAY1_PATH = "blueFrame:blueFrame_body:customers:0:rows:0:days:0";
    private static final String DAY1_FULL_PATH = TIMESHEET_PATH + ":" + DAY1_PATH;
    private static final User USER = new User(1);
    private static final String DAY_INPUT = ":day:day";

    @Mock
    private IPersistTimesheet persistTimesheet;

    @Mock
    private IOverviewTimesheet overviewTimesheet;

    @Mock
    private UserService userService;

    @Mock
    private TimesheetLockService lockService;


    private DateTime jodaNow = DateTime.now().withTimeAtStartOfDay();
    private Date dateNow = jodaNow.toDate();
    private GregorianCalendar calNow = jodaNow.toGregorianCalendar();
    private Date tomorrow = jodaNow.plusDays(1).toDate();
    private final ArrayList<Date> tomorrowAsList = Lists.newArrayList(tomorrow);

    @Before
    public void setup() {
        getConfig().setCompleteDayHours(8l);
        EhourWebSession.getSession().reloadConfig();

        getMockContext().putBean(persistTimesheet);
        getMockContext().putBean(overviewTimesheet);
        getMockContext().putBean("userService", userService);
        getMockContext().putBean(lockService);

        getMockContext().putBean(new TimesheetModel());
        TimesheetIconRenderFactoryCollection iconRenderFactoryCollection = new TimesheetIconRenderFactoryCollection(Lists.newArrayList(new DailyCommentPanelFactory()));
        getMockContext().putBean(iconRenderFactoryCollection);

        SectionRenderFactoryCollection sectionRenderFactoryCollection = new SectionRenderFactoryCollection(Lists.<SectionRenderFactory>newArrayList(new WeeklyCommentPanelRenderFactory()));
        getMockContext().putBean(sectionRenderFactoryCollection);
    }

    @Test
    public void addDayComment() {
        startAndReplayWithDefaultWeekOverview();

        final String comment = "commentaar";

        ModalWindow window = openCommentWindow(DAY1_FULL_PATH);
        assertTrue(window.isShown());

        FormTester timesheetFormTester = tester.newFormTester(TIMESHEET_PATH);
        timesheetFormTester.setValue(DAY1_PATH + ":options:1:dayWin:content:comment", comment);

        tester.executeAjaxEvent(DAY1_FULL_PATH + ":options:1:dayWin:content:comment", "onchange");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":options:1:dayWin:content:submit", "onclick");

        Timesheet timesheet = ((TimesheetContainer) tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject()).getTimesheet();
        assertEquals(comment, timesheet.getTimesheetEntries().get(0).getComment());

        tester.assertNoErrorMessage();
    }

    private ModalWindow openCommentWindow(String path) {
        ModalWindow window = (ModalWindow) tester.getComponentFromLastRenderedPage(path + ":options:1:dayWin");
        tester.executeAjaxEvent(path + ":options:1:commentLink", "onclick");
        return window;
    }

    @Test
    public void addDayCommentCancelled() {
        startAndReplayWithDefaultWeekOverview();

        final String comment = "commentaar";

        clickDay1();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);
        formTester.setValue(DAY1_PATH + ":options:1:dayWin:content:comment", comment);

        tester.executeAjaxEvent(DAY1_FULL_PATH + ":options:1:dayWin:content:comment", "onchange");
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":options:1:dayWin:content:cancel", "onclick");

        Timesheet timesheet = ((TimesheetContainer) tester.getComponentFromLastRenderedPage("panel").getDefaultModelObject()).getTimesheet();
        assertNull(timesheet.getTimesheetEntries().get(0).getComment());

        tester.assertNoErrorMessage();
    }

    private void clickDay1() {
        tester.executeAjaxEvent(DAY1_FULL_PATH + ":options:1:commentLink", "onclick");
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

        formTester.setValue(DAY1_PATH + DAY_INPUT, "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + DAY_INPUT, "onblur");
        tester.assertNoErrorMessage();

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

        formTester.setValue(DAY1_PATH + DAY_INPUT, "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + DAY_INPUT, "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains(DAY1_PATH + DAY_INPUT);

        //changing another field should not resend the unmodified day1
        formTester.setValue("blueFrame:blueFrame_body:customers:0:rows:0:days:1:day:day", "8");
        tester.executeAjaxEvent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:1:day:day", "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains("blueFrame:blueFrame_body:customers:0:rows:0:days:1:day:day");
    }

    @Test
    public void shouldResetErrorState() {
        startAndReplayWithDefaultWeekOverview();

        FormTester formTester = tester.newFormTester(TIMESHEET_PATH);

        formTester.setValue(DAY1_PATH + DAY_INPUT, "12");
        tester.executeAjaxEvent(DAY1_FULL_PATH + DAY_INPUT, "onblur");
        tester.assertNoErrorMessage();
        tester.assertContains(DAY1_PATH + DAY_INPUT);
        tester.assertContainsNot("color: #ff0000");

        formTester.setValue(DAY1_PATH + DAY_INPUT, "ff");
        tester.executeAjaxEvent(DAY1_FULL_PATH + DAY_INPUT, "onblur");
        tester.assertContains(DAY1_PATH + DAY_INPUT);
        tester.assertContains("color: #ff0000");
        tester.assertErrorMessages("day.IConverter.Float");

        formTester.setValue(DAY1_PATH + DAY_INPUT, "1");
        tester.executeAjaxEvent(DAY1_FULL_PATH + DAY_INPUT, "onblur");
        tester.assertContains(DAY1_PATH + DAY_INPUT);
        tester.assertContainsNot("color: #ff0000");
    }


    @SuppressWarnings("unchecked")
    @Test
    public void shouldSubmitSuccessful() {
        when(persistTimesheet.persistTimesheetWeek(any(Collection.class), any(TimesheetComment.class), any(DateRange.class), any(User.class)))
                .thenReturn(new ArrayList<ProjectAssignmentStatus>());

        startAndReplayWithDefaultWeekOverview();

        tester.executeAjaxEvent(TIMESHEET_PATH + ":submitButton", "onclick");

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldDisableInputForLockedDays() {
        startWithLockedDays(Arrays.asList(tomorrow));

        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:1:day:day", Label.class);
        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:2:day:day", TimesheetTextField.class);

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldDisableDailyCommentInputForLockedDaysWithExistingComment() {
        // given
        List<Date> lockedDays = tomorrowAsList;
        startWithLockedDays(lockedDays);

        DateRange nextWeekRange = new DateRange(dateNow, jodaNow.plusWeeks(1).toDate());

        TimesheetEntry timesheetEntry = TimesheetEntryObjectMother.createTimesheetEntry(1, tomorrow, 5);
        timesheetEntry.setComment("Comment");

        List<TimesheetEntry> entries = Arrays.asList(timesheetEntry);
        List<ProjectAssignment> assignments = Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1));

        WeekOverview overview = new WeekOverview(entries, null, assignments, nextWeekRange, USER, lockedDays);

        whenDefaultWeekOverview(overview);

        // when
        tester.startComponentInPage(new TimesheetPanel("panel", USER, new GregorianCalendar()));

        // then
        openCommentWindow(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:1");

        tester.assertComponent(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:1:options:1:dayWin:content:comment", Label.class);

        tester.assertNoErrorMessage();
        tester.assertNoInfoMessage();
    }

    @Test
    public void shouldHideCommentInputLinkForLockedDaysWithoutComment() {
        startWithLockedDays(tomorrowAsList);

        Component commentLink = tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:days:1:options:1:commentLink");
        assertNull(commentLink); // null = not visible...

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldAddLockedIconInDayForLockedDay() {
        startWithLockedDays(tomorrowAsList);

        String path = TIMESHEET_PATH + ":blueFrame:blueFrame_body:day2Label:lock:lockedContainer";
        tester.assertVisible(path);

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldHideBookWholeWeekIconWhenDisabledInConfig() {
        webApp.setEnableBookWholeWeek(false);

        startAndReplayWithDefaultWeekOverview();

        Component book = tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:bookWholeWeek");
        // not visible = null
        assertNull(book);
    }

    @Test
    public void shouldHideBookWholeWeekIconWhenADayIsLocked() {
        startWithLockedDays(tomorrowAsList);

        Component book = tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:customers:0:rows:0:bookWholeWeek");
        // not visible = null
        assertNull(book);
    }

    @Test
    public void shouldHideStoreButtonsWhenWholeWeekIsLocked() {
        List<Date> dates = Lists.newArrayList();

        for (int i = 0; i < 8; i++) {
            dates.add(jodaNow.plusDays(i).toDate());
        }

        startWithLockedDays(dates);

        assertNull(tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":submitButton"));
        assertNull(tester.getComponentFromLastRenderedPage(TIMESHEET_PATH + ":blueFrame:blueFrame_body:submitButtonTop"));
    }

    private void startAndReplayWithDefaultWeekOverview() {
        startWithLockedDays(Lists.<Date>newArrayList());
    }

    private void startWithLockedDays(List<Date> lockedDays) {
        whenDefaultWeekOverview(withDefaultWeekOverview(lockedDays));

        tester.startComponentInPage(new TimesheetPanel("panel", USER, calNow));
    }

    private void whenDefaultWeekOverview(WeekOverview overview) {
        when(overviewTimesheet.getWeekOverview(any(User.class), any(Calendar.class))).thenReturn(overview);
    }

    private WeekOverview withDefaultWeekOverview(List<Date> lockedDates) {
        DateRange nextWeekRange = new DateRange(dateNow, jodaNow.plusWeeks(1).toDate());

        List<TimesheetEntry> entries = Arrays.asList(TimesheetEntryObjectMother.createTimesheetEntry(1, dateNow, 5));
        List<ProjectAssignment> assignments = Arrays.asList(ProjectAssignmentObjectMother.createProjectAssignment(1));

        return new WeekOverview(entries, null, assignments, nextWeekRange, USER, lockedDates);
    }
}
