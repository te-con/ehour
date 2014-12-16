package net.rrm.ehour.ui.report.page;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.customer.CustomerAggregateReportModel;
import net.rrm.ehour.ui.report.customer.CustomerReportPanel;
import net.rrm.ehour.ui.report.detailed.DetailedReportModel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.project.ProjectAggregateReportModel;
import net.rrm.ehour.ui.report.project.ProjectReportPanel;
import net.rrm.ehour.ui.report.user.UserAggregateReportModel;
import net.rrm.ehour.ui.report.user.UserReportPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultReportTabBuilder implements ReportTabBuilder {
    private static final long serialVersionUID = -2377556952455077542L;

    public List<ITab> createReportTabs(ReportCriteriaBackingBean backingBean) {
        List<ITab> tabs = new ArrayList<ITab>();

        final ReportCriteria criteria = backingBean.getReportCriteria();

        tabs.add(new AbstractTab(new KeyResourceModel("report.title.customer")) {
            @Override
            public Panel getPanel(String panelId) {
                return getCustomerReportPanel(panelId, criteria);
            }
        });

        tabs.add(new AbstractTab(new KeyResourceModel("report.title.project")) {
            @Override
            public Panel getPanel(String panelId) {
                return getProjectReportPanel(panelId, criteria);
            }
        });

        if (!backingBean.getReportCriteria().getUserSelectedCriteria().isForIndividualUser()) {
            tabs.add(new AbstractTab(new KeyResourceModel("report.title.employee")) {
                @Override
                public Panel getPanel(String panelId) {
                    return getUserReportPanel(panelId, criteria);
                }
            });
        }

        tabs.add(new AbstractTab(new KeyResourceModel("report.title.detailed")) {
            @Override
            public Panel getPanel(String panelId) {
                return getDetailedReportPanel(panelId, criteria);
            }
        });

        return tabs;
    }

    private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria) {
        DetailedReportModel detailedReport = createDetailedReport(reportCriteria);
        return new DetailedReportPanel(id, detailedReport);
    }

    private DetailedReportModel createDetailedReport(ReportCriteria reportCriteria) {
        return new DetailedReportModel(reportCriteria);
    }

    private Panel getCustomerReportPanel(String id, ReportCriteria reportCriteria) {
        CustomerAggregateReportModel customerAggregateReport = new CustomerAggregateReportModel(reportCriteria);
        return new CustomerReportPanel(id, customerAggregateReport);
    }

    private Panel getProjectReportPanel(String id, ReportCriteria reportCriteria) {
        ProjectAggregateReportModel aggregateReport = new ProjectAggregateReportModel(reportCriteria);
        return new ProjectReportPanel(id, aggregateReport);
    }

    private Panel getUserReportPanel(String id, ReportCriteria reportCriteria) {
        UserAggregateReportModel aggregateReport = new UserAggregateReportModel(reportCriteria);
        return new UserReportPanel(id, aggregateReport);
    }
}
