package net.rrm.ehour.ui.report.trend.node;

import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.node.ReportNode;

import java.io.Serializable;

public class FlatLockableDateNode extends ReportNode {
    protected FlatLockableDateNode(Serializable id) {
        super(id);
    }

    @Override
    protected Serializable getElementId(ReportElement element) {
        return null;
    }
}
