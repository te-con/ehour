package net.rrm.ehour.ui.report.excel;

import org.junit.Test;

public class ProjectReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        generateAndAssert(new ProjectReportExcel(criteriaModel));
    }
}
