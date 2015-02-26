package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.report.excel.IWriteBytes;
import org.apache.wicket.util.io.IClusterable;

public interface ExcelReport extends IClusterable, IWriteBytes {
    String getFilenameWihoutSuffix();
}
