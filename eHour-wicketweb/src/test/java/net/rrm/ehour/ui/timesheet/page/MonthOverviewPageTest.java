package net.rrm.ehour.ui.timesheet.page;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.approvalstatus.service.ApprovalStatusService;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElementMother;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.dto.UserProjectStatus;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.MockExpectations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.mockito.Matchers.isNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MonthOverviewPageTest extends BaseSpringWebAppTester {
    @Mock
    private IOverviewTimesheet overviewTimesheet;

    @Mock
    private ApprovalStatusService approvalStatusService;

    @Mock
    private WindChillUpdateService windChillUpdateService;

    @Mock
    private JiraService jiraService;

    @Test
    public void testOverviewPageRender() {
        getMockContext().putBean(overviewTimesheet);
        getMockContext().putBean(approvalStatusService);
        getMockContext().putBean(windChillUpdateService);
        getMockContext().putBean(jiraService);

        MockExpectations.navCalendarMockito(overviewTimesheet, getWebApp());

        TimesheetOverview overview = new TimesheetOverview();
        ActivityAggregateReportElement aggregate = ActivityAggregateReportElementMother.createActivityAggregate(1, 1, 1);

        UserProjectStatus status = new UserProjectStatus(aggregate, 5);
        overview.setProjectStatus(Sets.newTreeSet(Lists.newArrayList(status)));

        when(overviewTimesheet.getTimesheetOverview(isNotNull(User.class), isNotNull(Calendar.class))).thenReturn(overview);

        getTester().startPage(MonthOverviewPage.class);
        getTester().assertRenderedPage(MonthOverviewPage.class);
        getTester().assertNoErrorMessage();

        tester.debugComponentTrees();
    }
}
