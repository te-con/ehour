package net.rrm.ehour.ui.report.excel;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class CustomerReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        assertNotNull(new CustomerReportExcel().getExcelData(criteria));
    }
}
