package net.rrm.ehour.ui.report.builder;

import net.rrm.ehour.report.criteria.ReportCriteria;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface ReportTabFactory {
    ITab createReportTab(ReportCriteria criteria);

    int getRenderPriority();
}
