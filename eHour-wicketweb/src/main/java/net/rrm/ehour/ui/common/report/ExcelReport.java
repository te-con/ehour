package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import org.apache.wicket.util.io.IClusterable;

public interface ExcelReport extends IClusterable{
    byte[] getExcelData(ReportCriteria reportCriteria);

    String getFilename();
}
