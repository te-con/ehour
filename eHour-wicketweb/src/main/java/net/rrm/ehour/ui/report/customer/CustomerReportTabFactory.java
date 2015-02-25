package net.rrm.ehour.ui.report.customer;

import com.google.common.base.Optional;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.builder.ReportFactory;
import net.rrm.ehour.ui.report.builder.ReportTabFactory;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

@ReportFactory
public class CustomerReportTabFactory implements ReportTabFactory {
    @Override
    public Optional<ITab> createReportTab(final ReportCriteria criteria) {
        return Optional.<ITab>of(new AbstractTab(new KeyResourceModel("report.title.customer")) {
            @Override
            public Panel getPanel(String panelId) {
                return getCustomerReportPanel(panelId, criteria);
            }
        });
    }

    @Override
    public int getRenderPriority() {
        return 0;
    }

    private Panel getCustomerReportPanel(String id, ReportCriteria reportCriteria) {
        CustomerAggregateReportModel customerAggregateReport = new CustomerAggregateReportModel(reportCriteria);
        return new CustomerReportPanel(id, customerAggregateReport);
    }

}
