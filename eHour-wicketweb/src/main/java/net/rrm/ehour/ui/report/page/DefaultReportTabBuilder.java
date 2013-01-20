package net.rrm.ehour.ui.report.page;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReportModel;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.trend.DetailedReportModel;
import net.rrm.ehour.ui.report.util.ReportUtil;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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

        tabs.add(new AbstractTab(new KeyResourceModel("report.title.employee")) {
            @Override
            public Panel getPanel(String panelId) {
                return getUserReportPanel(panelId, criteria);
            }
        });

        tabs.add(new AbstractTab(new KeyResourceModel("report.title.detailed")) {
            @Override
            public Panel getPanel(String panelId) {
                return getDetailedReportPanel(panelId, criteria);
            }
        });

        return tabs;
    }

    private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria)
    {
        DetailedReportModel detailedReport = createAndCacheDetailedReport(reportCriteria);
        return new DetailedReportPanel(id, detailedReport);
    }

    private DetailedReportModel createAndCacheDetailedReport(ReportCriteria reportCriteria) {
        DetailedReportModel detailedReport = new DetailedReportModel(reportCriteria);
        ReportUtil.storeInCache(detailedReport);
        return detailedReport;
    }

    private Panel getCustomerReportPanel(String id, ReportCriteria reportCriteria) {
        CustomerAggregateReportModel customerAggregateReport = new CustomerAggregateReportModel(reportCriteria);
        ReportUtil.storeInCache(customerAggregateReport);
        return new CustomerReportPanel(id, customerAggregateReport);
    }

    private Panel getProjectReportPanel(String id, ReportCriteria reportCriteria) {
        ProjectAggregateReportModel aggregateReport = new ProjectAggregateReportModel(reportCriteria);
        ReportUtil.storeInCache(aggregateReport);
        return new ProjectReportPanel(id, aggregateReport);
    }

    private Panel getUserReportPanel(String id, ReportCriteria reportCriteria) {
        UserAggregateReportModel aggregateReport = new UserAggregateReportModel(reportCriteria);
        ReportUtil.storeInCache(aggregateReport);
        return new EmployeeReportPanel(id, aggregateReport);
    }
}
