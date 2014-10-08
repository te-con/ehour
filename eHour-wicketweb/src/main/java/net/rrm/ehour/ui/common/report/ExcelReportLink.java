package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import net.rrm.ehour.ui.common.util.Function;
import org.apache.wicket.markup.html.link.Link;

public abstract class ExcelReportLink extends Link<Void> {

    private final ReportCriteria reportCriteria;

    public ExcelReportLink(String id, ReportCriteria reportCriteria) {
        super(id);
        this.reportCriteria = reportCriteria;
    }

    @Override
    public void onClick() {
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(createReportBuilder().getFilename(), new Function<byte[]>() {
            @Override
            public byte[] apply() {
                return createReportBuilder().getExcelData(reportCriteria);
            }
        }));
    }

    protected abstract ExcelReport createReportBuilder();
}
