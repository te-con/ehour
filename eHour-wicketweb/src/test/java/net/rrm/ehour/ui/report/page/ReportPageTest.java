package net.rrm.ehour.ui.report.page;

import com.google.common.collect.Sets;
import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ActivityAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("serial")
public class ReportPageTest extends BaseSpringWebAppTester implements Serializable {
    private MockReportTabBuilder mockReportTabCommand;
    protected ReportCriteriaService reportCriteriaService;
    protected AggregateReportService aggregateReportService;
    protected DetailedReportService detailedReportService;
    protected ReportData data;
    protected ReportCriteria reportCriteria;

    @Before
    public void setup() {
        mockReportTabCommand = new MockReportTabBuilder();

        getMockContext().putBean("reportTabBuilder", mockReportTabCommand);

        WindChillUpdateService windChillUpdateService = createMock(WindChillUpdateService.class);
        getMockContext().putBean(windChillUpdateService);

        JiraService jiraService = createMock(JiraService.class);
        getMockContext().putBean(jiraService);
    }

    @Test
    public void shouldRender() {
        expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class), eq(ReportCriteriaUpdateType.UPDATE_ALL)))
                .andReturn(reportCriteria);

        replay(reportCriteriaService);

        startPage();

        verify(reportCriteriaService);
    }

    @Test
    public void shouldUpdateTabs() {
        mockReportTabCommand.returnTabs = createTabs(3);

        updateTabs();

        assertNotNull(mockReportTabCommand.argBean);
    }

    private void updateTabs() {
        expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class),
                eq(ReportCriteriaUpdateType.UPDATE_ALL)))
                .andReturn(reportCriteria);

        replay(reportCriteriaService);

        startPage();

        ReportPage page = (ReportPage) tester.getLastRenderedPage();

        AjaxRequestTarget target = createMock(AjaxRequestTarget.class);
        AjaxEvent event = new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED, target);

        target.add(isA(ReportTabbedPanel.class));
        replay(target);

        page.ajaxEventReceived(event);

        verify(target);
        verify(reportCriteriaService);
    }

    private List<ITab> createTabs(int amount) {
        List<ITab> tabs = new ArrayList<ITab>();

        for (int i = 0; i < amount; i++) {
            AbstractTab tab = new AbstractTab(new KeyResourceModel(Integer.toString(i))) {

                @Override
                public Panel getPanel(String panelId) {
                    return new EmptyPanel(panelId);
                }
            };

            tabs.add(tab);
        }

        return tabs;

    }

    private void startPage() {
        tester.startPage(ReportPage.class);

        tester.assertRenderedPage(ReportPage.class);
        tester.assertNoErrorMessage();
    }

    @Before
    public final void before() throws Exception {
        reportCriteriaService = createMock(ReportCriteriaService.class);
        getMockContext().putBean("reportCriteriaService", reportCriteriaService);

        aggregateReportService = createMock(AggregateReportService.class);
        getMockContext().putBean("aggregatedReportService", aggregateReportService);

        detailedReportService = createMock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);

        AvailableCriteria availCriteria = new AvailableCriteria();

        SortedSet<Customer> customers = Sets.newTreeSet();
        customers.add(new Customer(1));
        availCriteria.setCustomers(customers);

        Set<Project> projects = Sets.newHashSet();
        projects.add(new Project(2));
        availCriteria.setProjects(projects);

        Set<User> usrs = Sets.newHashSet();
        usrs.add(new User(2));
        availCriteria.setUsers(usrs);

        reportCriteria = new ReportCriteria(availCriteria);

        List<ActivityAggregateReportElement> agg = new ArrayList<ActivityAggregateReportElement>();
        ActivityAggregateReportElement pag = new ActivityAggregateReportElement();
        Activity activity = ActivityMother.createActivity(1);
        User user = new User(1);
        activity.setAssignedUser(user);

        Customer cust = new Customer(1);
        Project prj = new Project(1);
        prj.setCustomer(cust);
        activity.setProject(prj);
        pag.setActivity(activity);

        agg.add(pag);

        data = new ReportData(agg, reportCriteria.getReportRange(), reportCriteria.getUserSelectedCriteria());
    }

    private class MockReportTabBuilder implements ReportTabBuilder {
        ReportCriteriaBackingBean argBean;
        List<ITab> returnTabs;

        public List<ITab> createReportTabs(ReportCriteriaBackingBean backingBean) {
            this.argBean = backingBean;
            return returnTabs;
        }

    }
}
