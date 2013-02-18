package net.rrm.ehour.ui.common.report.excel;

import net.rrm.ehour.ui.common.util.Function;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ExcelRequestHandler implements IRequestHandler {

    private String filename;
    private Function<byte[]> reportConstructionFunction;

    public ExcelRequestHandler(String filename, Function<byte[]> reportConstructionFunction) {
        this.filename = filename;
        this.reportConstructionFunction = reportConstructionFunction;
    }

    @Override
    public void detach(IRequestCycle requestCycle) {
    }

    @Override
    public void respond(IRequestCycle requestCycle) {
        try {

            HttpServletResponse httpResponse = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();
            httpResponse.setContentType("application/vnd.ms-excel");
            httpResponse.setHeader("Content-disposition", "attachment; filename=" + filename);
            ServletOutputStream outputStream = httpResponse.getOutputStream();

            outputStream.write(reportConstructionFunction.apply());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
