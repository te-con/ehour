package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.report.excel.ExcelRequestHandler;
import net.rrm.ehour.ui.common.report.excel.IWriteBytes;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.link.Link;

public class XlsxLink extends Link<Void> {

    private final String filename;
    private final IWriteBytes byteWriter;

    public XlsxLink(String id, String filename, IWriteBytes byteWriter) {
        super(id);
        this.filename = filename;
        this.byteWriter = byteWriter;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(AttributeModifier.append("title", getString("excel.export.tooltip")));
    }

    @Override
    public void onClick() {
        getRequestCycle().scheduleRequestHandlerAfterCurrent(new ExcelRequestHandler(filename, byteWriter, ExcelRequestHandler.Format.XLSX));
    }
}
