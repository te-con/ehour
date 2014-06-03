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

package net.rrm.ehour.ui.report.trend;


import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

public class DetailedReportTest extends BaseSpringWebAppTester {
    private DetailedReportService detailedReportService;

    @Before
    public void setup() throws Exception {
        detailedReportService = createMock(DetailedReportService.class);
        getMockContext().putBean("detailedReportService", detailedReportService);
    }

    @Test
    public void testCreateDetailedReport() {
        expect(detailedReportService.getDetailedReportData(isA(ReportCriteria.class)))
                .andReturn(DetailedReportDataObjectMother.getFlatReportData());

        replay(detailedReportService);

        DetailedReportModel detailedReport = new DetailedReportModel(DetailedReportDataObjectMother.getReportCriteria());
        assertEquals(5, detailedReport.getReportData().getReportElements().size());

        verify(detailedReportService);
    }
}
