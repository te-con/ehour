package net.rrm.ehour.ui.report.excel;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class UserReportExcelTest extends AbstractReportExcelTest {
    @Test
    public void should_generate() throws Exception {
        assertNotNull(UserReportExcel.getInstance().getExcelData(criteria));
    }
}
