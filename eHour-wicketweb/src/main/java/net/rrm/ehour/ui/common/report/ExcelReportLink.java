package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import org.apache.wicket.markup.html.link.Link;


// merge with xlsxlink
public abstract class ExcelReportLink extends Link<Void> {

    public ExcelReportLink(String id) {
        super(id);
    }

    @Override
    public void onClick() {
        ExcelReport reportBuilder = createReportBuilder();
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(reportBuilder.getFilename(), reportBuilder, ExcelRequestHandler.Format.XLSX));
    }

    protected abstract ExcelReport createReportBuilder();
}
