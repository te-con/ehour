package net.rrm.ehour.ui.report.page;

import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import org.apache.wicket.extensions.markup.html.tabs.ITab;

import java.io.Serializable;
import java.util.List;

public interface ReportTabBuilder extends Serializable {
    /**
     * Create report tabs
     *
     * @param backingBean
     * @return
     */
    List<ITab> createReportTabs(ReportCriteriaBackingBean backingBean);
}
