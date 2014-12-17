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

package net.rrm.ehour.ui.report.detailed;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.report.cache.ReportCacheService;
import org.apache.wicket.markup.html.basic.Label;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Detailed report panel test
 */

public class DetailedReportPanelTest extends BaseSpringWebAppTester {
    private DetailedReportService detailedReportService;

    @Before
    public void setup() {
        detailedReportService = mock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);

        ReportCacheService reportCacheService = mock(ReportCacheService.class);
        getMockContext().putBean("reportCacheService", reportCacheService);
    }

    @Test
    @SuppressWarnings("serial")
    public void shouldRenderPanel() {
        when(detailedReportService.getDetailedReportData(any(ReportCriteria.class)))
                .thenReturn(DetailedReportDataObjectMother.getFlatReportData());
        when(detailedReportService.getDetailedReportData(any(ReportCriteria.class)))
                .thenReturn(DetailedReportDataObjectMother.getFlatReportData());

        DetailedReportModel detailedReport = new DetailedReportModel(DetailedReportDataObjectMother.getReportCriteria());

        tester.startComponentInPage(new DetailedReportPanel("id", detailedReport));

        tester.assertComponent("id:reportTable:greyFrame:greyFrame_body:reportContent:reportFrame:reportFrame_body:reportFrameContainer:cell:2", Label.class);

        tester.assertNoErrorMessage();
    }
}
