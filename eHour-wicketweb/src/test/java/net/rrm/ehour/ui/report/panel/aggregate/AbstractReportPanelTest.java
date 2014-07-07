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

package net.rrm.ehour.ui.report.panel.aggregate;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.report.TreeReportModel;
import org.apache.wicket.markup.html.panel.Panel;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * Created on Mar 17, 2009, 6:39:39 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public abstract class AbstractReportPanelTest extends BaseSpringWebAppTester {
    private AggregateReportService aggregateReportService;


    @Before
    public void setup() {
        aggregateReportService = createMock(AggregateReportService.class);
        getMockContext().putBean("aggregateReportService", aggregateReportService);
    }

    @Test
    public void shouldRenderReportPanel() {
        setupExpectations();

        replay(aggregateReportService);

        startReportPanel();

        tester.assertNoErrorMessage();

        verify(aggregateReportService);
    }

    protected void setupExpectations() {
        expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
                .andReturn(AggregateReportDataObjectMother.getAssignmentReportData())
                .anyTimes();
    }

    protected void startReportPanel() {
        TreeReportModel model = getAggregateReport();
        tester.startComponentInPage(createReportPanel("id", model));
    }

    public AggregateReportService getAggregateReportService() {
        return aggregateReportService;
    }

    protected abstract TreeReportModel getAggregateReport();

    protected abstract Panel createReportPanel(String panelId, TreeReportModel reportModel);
}