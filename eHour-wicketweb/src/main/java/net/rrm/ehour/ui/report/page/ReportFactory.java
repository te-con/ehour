package net.rrm.ehour.ui.report.page;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

public interface ReportFactory {
    ITab createReportTab(ReportCriteriaBackingBean criteriaBackingBean);
}
