package net.rrm.ehour.ui.report.user;

import com.google.common.base.Optional;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.builder.ReportFactory;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

@ReportFactory
public class UserReportTabFactory implements ReportTabFactory {
    @Override
    public Optional<ITab> createReportTab(final ReportCriteria criteria) {
        if (!criteria.getUserSelectedCriteria().isForIndividualUser()) {
            return Optional.<ITab>of(new AbstractTab(new KeyResourceModel("report.title.employee")) {
                @Override
                public Panel getPanel(String panelId) {
                    return getUserReportPanel(panelId, criteria);
                }
            });
        } else {
            return Optional.absent();
        }
    }

    private Panel getUserReportPanel(String id, ReportCriteria reportCriteria) {
        UserAggregateReportModel aggregateReport = new UserAggregateReportModel(reportCriteria);
        return new UserReportPanel(id, aggregateReport);
    }

    @Override
    public int getRenderPriority() {
        return 3;
    }

}
