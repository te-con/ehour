package net.rrm.ehour.ui.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.report.AbstractExcelReport;
import net.rrm.ehour.ui.report.detailed.DetailedReportDataObjectMother;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public abstract class AbstractReportExcelTest extends BaseSpringWebAppTester {
    protected IModel<ReportCriteria> criteriaModel;
    private AggregateReportService aggregateReportService;

    @Before
    public void before() throws Exception {
        aggregateReportService = mock(AggregateReportService.class);
        getMockContext().putBean("aggregateReportService", aggregateReportService);

        criteriaModel = new Model<>(DetailedReportDataObjectMother.getReportCriteria());

        when(aggregateReportService.getAggregateReportData(criteriaModel.getObject()))
                .thenReturn(AggregateReportDataObjectMother.getAssignmentReportData());
    }

    @After
    public void tearDown() {
        verify(aggregateReportService).getAggregateReportData(criteriaModel.getObject());

    }

    protected void generateAndAssert(AbstractExcelReport report) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        report.write(stream);
        byte[] excelData = stream.toByteArray();
        assertTrue(excelData.length > 0);
    }
}