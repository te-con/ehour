package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class DetailedReportExcelTest extends BaseSpringWebAppTester {
    @Mock
    private DetailedReportService detailedReportService;

    @Before
    public void set_up() {
        MockitoAnnotations.initMocks(this);

        getMockContext().putBean("detailedReportService", detailedReportService);
    }

    @Test
    public void should_generate() throws Exception {
        ReportCriteria criteria = DetailedReportDataObjectMother.getReportCriteria();

        when(detailedReportService.getDetailedReportData(criteria)).thenReturn(DetailedReportDataObjectMother.getFlatReportData());

        assertNotNull(DetailedReportExcel.getInstance().getExcelData(criteria));
    }
}
