package net.rrm.ehour.ui.report.detailed.node;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.model.ReportNode;

import java.io.Serializable;

public class FlatProjectCodeNode extends ReportNode {
    private static final long serialVersionUID = -9117864025503755613L;

    public FlatProjectCodeNode(FlatReportElement element) {

        super(element.getProjectId(), element.isEmptyEntry());
        this.columnValues = new String[]{element.getProjectCode()};
    }

    @Override
    protected Serializable getElementId(ReportElement element) {
        return ((FlatReportElement) element).getProjectId();
    }

}
