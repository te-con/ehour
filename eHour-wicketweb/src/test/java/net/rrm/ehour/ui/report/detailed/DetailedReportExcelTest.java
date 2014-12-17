package net.rrm.ehour.ui.report.detailed;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.service.DetailedReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.wicket.Model;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertTrue;
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

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        new DetailedReportExcel(new Model<ReportCriteria>(criteria)).write(stream);
        byte[] excelData = stream.toByteArray();
        assertTrue(excelData.length > 0);
    }
}
