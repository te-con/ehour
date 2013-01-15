package net.rrm.ehour.ui.report.excel;

import net.rrm.ehour.ui.report.TreeReportModel;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReportModel;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class ProjectReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        TreeReportModel reportModel = new ProjectAggregateReportModel(criteria);

        assertNotNull(new ProjectReportExcel().getExcelData(reportModel));
    }
}
