package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.AggregateReportDataObjectMother;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.report.panel.DetailedReportDataObjectMother;
import org.junit.After;
import org.junit.Before;

import static org.easymock.EasyMock.*;

public class AbstractReportExcelTest extends AbstractSpringWebAppTester {
    protected ReportCriteria criteria;
    private AggregateReportService aggregateReportService;

    @Before
    public void before() throws Exception {
        aggregateReportService = createMock(AggregateReportService.class);
        getMockContext().putBean("aggregateReportService", aggregateReportService);

        criteria = DetailedReportDataObjectMother.getReportCriteria();

        expect(aggregateReportService.getAggregateReportData(isA(ReportCriteria.class)))
                .andReturn(AggregateReportDataObjectMother.getAssignmentReportData());

        replay(aggregateReportService);
    }

    @After
    public void tearDown() {
        verify(aggregateReportService);
    }
}