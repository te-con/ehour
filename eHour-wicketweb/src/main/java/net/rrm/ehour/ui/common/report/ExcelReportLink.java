package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import org.apache.wicket.markup.html.link.Link;


// merge with xlsxlink
public abstract class ExcelReportLink extends Link<Void> {

    private static final String FILE_EXTENSION = "xlsx";

    public ExcelReportLink(String id) {
        super(id);
    }

    @Override
    public void onClick() {
        ExcelReport reportBuilder = createReportBuilder();
        String filename = String.format("%s.%s", reportBuilder.getFilenameWihoutSuffix(), FILE_EXTENSION);
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(filename, reportBuilder, ExcelRequestHandler.Format.XLSX));
    }

    protected abstract ExcelReport createReportBuilder();
}
