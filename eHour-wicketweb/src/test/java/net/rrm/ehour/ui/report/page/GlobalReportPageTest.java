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

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.ReportCriteriaUpdateType;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.tester.ITestPageSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("serial")
public class GlobalReportPageTest extends BaseTestReport {
    private AggregateBuilder aggregateCommand;

    @Before
    public void setup() {
        aggregateCommand = new AggregateBuilder();

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
    public void shouldUpdateTabsForAggregate() {
        aggregateCommand.returnTabs = createTabs(3);

        shouldUpdateTabs();

        assertNotNull(aggregateCommand.argBean);
    }

    private void shouldUpdateTabs() {
        expect(reportCriteriaService.syncUserReportCriteria(isA(ReportCriteria.class),
                eq(ReportCriteriaUpdateType.UPDATE_ALL)))
                .andReturn(reportCriteria);

        replay(reportCriteriaService);

        startPage();

        Component component = tester.getComponentFromLastRenderedPage("");
        GlobalReportPage page = (GlobalReportPage) component;

        AjaxRequestTarget target = createMock(AjaxRequestTarget.class);
        AjaxEvent event = new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED, target);

        target.addComponent(isA(ReportTabbedPanel.class));
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
                    return new Panel(panelId);
                }
            };

            tabs.add(tab);
        }

        return tabs;

    }

    private void startPage() {
        getTester().startPage(new ITestPageSource() {

            public Page getTestPage() {
                return new GlobalReportPage(aggregateCommand);
            }
        });

        getTester().assertRenderedPage(GlobalReportPage.class);
        getTester().assertNoErrorMessage();
    }

    private class AggregateBuilder implements ReportTabBuilder {
        ReportCriteriaBackingBean argBean;
        List<ITab> returnTabs;

        public List<ITab> createAggregateReportTabs(ReportCriteriaBackingBean backingBean) {
            this.argBean = backingBean;
            return returnTabs;
        }

    }
}
