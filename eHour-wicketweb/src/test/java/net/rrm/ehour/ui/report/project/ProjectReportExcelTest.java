package net.rrm.ehour.ui.report.project;

import net.rrm.ehour.ui.report.AbstractReportExcelTest;
import org.junit.Test;

public class ProjectReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        generateAndAssert(new ProjectReportExcel(criteriaModel));
    }
}
