package net.rrm.ehour.ui.report.detailed;

import com.google.common.base.Optional;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.builder.ReportFactory;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

@ReportFactory
public class DetailedReportTabFactory implements ReportTabFactory {
    @Override
    public Optional<ITab> createReportTab(final ReportCriteria criteria) {
        return Optional.<ITab>of(new AbstractTab(new KeyResourceModel("report.title.detailed")) {
            @Override
            public Panel getPanel(String panelId) {
                return getDetailedReportPanel(panelId, criteria);
            }
        });
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
