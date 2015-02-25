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

package net.rrm.ehour.ui.report.page;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import net.rrm.ehour.ui.report.builder.ReportTabs;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@SuppressWarnings("serial")
public class ReportPageTest extends BaseSpringWebAppTester implements Serializable {
    private ReportCriteriaService reportCriteriaService;
    private ReportCriteria reportCriteria;

    private ReportTabs reportTabs;

    @Before
    public void setup() {
        reportTabs = new ReportTabs(Lists.<ReportTabFactory>newArrayList());
        getMockContext().putBean(reportTabs);
    }

    @Test
    public void shouldRender() {
        when(reportCriteriaService.syncUserReportCriteria(any(ReportCriteria.class), eq(ReportCriteriaUpdateType.UPDATE_ALL)))
                .thenReturn(reportCriteria);

        startPage();

        tester.assertNoErrorMessage();
    }

    @Test
    public void shouldUpdateTabs() {
        ReportTabFactory factory = mock(ReportTabFactory.class);

        when(factory.createReportTab(any(ReportCriteria.class))).thenReturn(
                Optional.<ITab>of(new AbstractTab(new KeyResourceModel("a")) {

                    @Override
                    public Panel getPanel(String panelId) {
                        return new EmptyPanel(panelId);
                    }
                })
        );

        reportTabs.setFactories(Lists.newArrayList(factory));

        updateTabs();

        verify(factory).createReportTab(any(ReportCriteria.class));
    }

    private void updateTabs() {
        when(reportCriteriaService.syncUserReportCriteria(any(ReportCriteria.class),
                eq(ReportCriteriaUpdateType.UPDATE_ALL)))
                .thenReturn(reportCriteria);

        startPage();

        ReportPage page = (ReportPage) tester.getLastRenderedPage();

        AjaxRequestTarget target = mock(AjaxRequestTarget.class);
        AjaxEvent event = new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED, target);

        page.ajaxEventReceived(event);

        verify(target).add(any(ReportTabbedPanel.class));
    }

    private void startPage() {
        tester.startPage(ReportPage.class);

        tester.assertRenderedPage(ReportPage.class);
        tester.assertNoErrorMessage();
    }

    @Before
    public final void before() throws Exception {
        reportCriteriaService = mock(ReportCriteriaService.class);
        getMockContext().putBean("reportCriteriaService", reportCriteriaService);

        AggregateReportService aggregateReportService = mock(AggregateReportService.class);
        getMockContext().putBean("aggregatedReportService", aggregateReportService);

        DetailedReportService detailedReportService = mock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);

        AvailableCriteria availCriteria = new AvailableCriteria();

        SortedSet<Customer> customers = Sets.newTreeSet();
        customers.add(new Customer(1));
        availCriteria.setCustomers(customers);

        Set<Project> projects = Sets.newHashSet();
        projects.add(new Project(2));
        availCriteria.setProjects(projects);

        List<UserDepartment> depts = new ArrayList<>();
        depts.add(new UserDepartment(2));
        availCriteria.setUserDepartments(depts);

        Set<User> usrs = Sets.newHashSet();
        usrs.add(new User(2));
        availCriteria.setUsers(usrs);

        reportCriteria = new ReportCriteria(availCriteria);
    }
}
