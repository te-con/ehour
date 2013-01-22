package net.rrm.ehour.ui.common.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public abstract class ExcelLink extends Link<Void> {

    private final ReportCriteria reportCriteria;

    public ExcelLink(String id, ReportCriteria reportCriteria) {
        super(id);
        this.reportCriteria = reportCriteria;
    }

    @Override
    public void onClick() {
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new IRequestHandler() {

            @Override
            public void detach(IRequestCycle requestCycle) {

            }

            @Override
            public void respond(IRequestCycle requestCycle) {

                try {

                    HttpServletResponse httpResponse = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();
                    httpResponse.setContentType("application/vnd.ms-excel");
                    httpResponse.setHeader("Content-disposition", "attachment; filename=" + createReportBuilder().getFilename());
                    ServletOutputStream outputStream = httpResponse.getOutputStream();

                    outputStream.write(createReportBuilder().getExcelData(reportCriteria));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    protected abstract ExcelReport createReportBuilder();
}
