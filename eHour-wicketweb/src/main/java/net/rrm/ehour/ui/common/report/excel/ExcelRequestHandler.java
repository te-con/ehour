package net.rrm.ehour.ui.common.report.excel;

import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.IRequestHandler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class ExcelRequestHandler implements IRequestHandler {
    public enum Format {
        XLS("application/vnd.ms-excel"),
        XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        final String mimetype;

        Format(String mimetype) {
            this.mimetype = mimetype;
        }
    }

    private String filename;
    private final IWriteBytes byteWriter;
    private final Format format;

    public ExcelRequestHandler(String filename, IWriteBytes byteWriter) {
        this(filename, byteWriter, Format.XLS);
    }


    public ExcelRequestHandler(String filename, IWriteBytes byteWriter, Format format) {
        this.filename = filename;
        this.byteWriter = byteWriter;
        this.format = format;
    }

    @Override
    public void detach(IRequestCycle requestCycle) {
    }

    @Override
    public void respond(IRequestCycle requestCycle) {
        try {
            HttpServletResponse httpResponse = (HttpServletResponse) requestCycle.getResponse().getContainerResponse();
            httpResponse.setContentType(format.mimetype);
            httpResponse.setHeader("Content-disposition", "attachment; filename=" + filename);
            ServletOutputStream outputStream = httpResponse.getOutputStream();

            byteWriter.write(outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
