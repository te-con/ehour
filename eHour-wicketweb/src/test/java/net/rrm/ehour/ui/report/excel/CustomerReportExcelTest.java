package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReportModel;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class CustomerReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        TreeReportModel reportModel = new CustomerAggregateReportModel(criteria);
        assertNotNull(new CustomerReportExcel().getExcelData(reportModel));
    }
}
