package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
import net.rrm.ehour.ui.report.trend.DetailedReportModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class DetailedReportExcelTest extends AbstractSpringWebAppTester {
    @Mock
    private DetailedReportService detailedReportService;

    @Before
    public void set_up() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void should_generate() throws Exception {
        ReportCriteria criteria = DetailedReportDataObjectMother.getReportCriteria();

        when(detailedReportService.getDetailedReportData(criteria)).thenReturn(DetailedReportDataObjectMother.getFlatReportData());

        TreeReportModel reportModel = new DetailedReportModel(criteria, detailedReportService);
        assertNotNull(new DetailedReportExcel().getExcelData(reportModel));
    }
}
