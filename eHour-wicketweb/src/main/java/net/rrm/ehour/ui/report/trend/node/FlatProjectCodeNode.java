package net.rrm.ehour.ui.report.trend.node;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

import java.io.Serializable;

public class FlatProjectCodeNode extends ReportNode {
    private static final long serialVersionUID = -9117864025503755613L;

    /**
     * @param element
     */
    public FlatProjectCodeNode(FlatReportElement element) {

        super(element.getProjectId());
        this.columnValues = new String[]{element.getProjectCode()};
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.report.node.ReportNode#getElementId(net.rrm.ehour.persistence.persistence.report.reports.importer.ReportElement)
      */
    @Override
    protected Serializable getElementId(ReportElement element) {
        return ((FlatReportElement) element).getProjectId();
    }

}
