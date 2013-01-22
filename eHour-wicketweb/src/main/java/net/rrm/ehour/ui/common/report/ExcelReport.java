package net.rrm.ehour.ui.common.report;

import org.apache.wicket.util.io.IClusterable;

import java.io.IOException;

public interface ExcelReport extends IClusterable{
    byte[] getExcelData(Report report) throws IOException;

    String getFilename();
}
