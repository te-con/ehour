package net.rrm.ehour.ui.report.customer;

import net.rrm.ehour.ui.report.AbstractReportExcelTest;
import org.junit.Test;

public class CustomerReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        generateAndAssert(new CustomerReportExcel(criteriaModel));
    }
}
