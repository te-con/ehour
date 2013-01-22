package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import org.apache.wicket.util.io.IClusterable;

import java.io.IOException;

public interface ExcelReport extends IClusterable{
    byte[] getExcelData(ReportCriteria reportCriteria) throws IOException;

    String getFilename();
}
