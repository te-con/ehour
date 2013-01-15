package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReportModel;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class UserReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        TreeReportModel reportModel = new UserAggregateReportModel(criteria);
        assertNotNull(new UserReportExcel().getExcelData(reportModel));
    }
}
