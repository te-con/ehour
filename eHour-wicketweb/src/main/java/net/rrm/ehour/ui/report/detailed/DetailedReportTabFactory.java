package net.rrm.ehour.ui.report.detailed;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.builder.ReportFactory;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

@ReportFactory
public class DetailedReportTabFactory implements ReportTabFactory {
    @Override
    public ITab createReportTab(final ReportCriteria criteria) {
        return new AbstractTab(new KeyResourceModel("report.title.detailed")) {
            @Override
            public Panel getPanel(String panelId) {
                return getDetailedReportPanel(panelId, criteria);
            }
        };
    }

    @Override
    public int getRenderPriority() {
        return 4;
    }

    private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria) {
        DetailedReportModel detailedReport = new DetailedReportModel(reportCriteria);
        return new DetailedReportPanel(id, detailedReport);
    }
}
