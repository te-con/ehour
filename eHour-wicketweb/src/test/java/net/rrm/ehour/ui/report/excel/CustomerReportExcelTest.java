package net.rrm.ehour.ui.report.excel;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CustomerReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        assertNotNull(CustomerReportExcel.getInstance().getExcelData(criteria));
    }
}
