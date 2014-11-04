package net.rrm.ehour.ui.report.excel;

import org.junit.Test;

public class UserReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        generateAndAssert(new UserReportExcel(criteriaModel));
    }
}
