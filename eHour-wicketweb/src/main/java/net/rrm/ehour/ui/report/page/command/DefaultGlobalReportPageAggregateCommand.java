package net.rrm.ehour.ui.report.page.command;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReportModel;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReportModel;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.util.ReportUtil;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;

import java.util.ArrayList;
import java.util.List;

public class DefaultGlobalReportPageAggregateCommand implements GlobalReportPageAggregateCommand
{
	private static final long serialVersionUID = -2377556952455077542L;

	public List<ITab> createAggregateReportTabs(ReportCriteriaBackingBean backingBean)
	{
		List<ITab> tabs = new ArrayList<ITab>();
		
		final ReportCriteria criteria = backingBean.getReportCriteria();
		
		ITab customerTab = new AbstractTab(new KeyResourceModel("report.title.customer"))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Panel getPanel(String panelId)
			{
				return getCustomerReportPanel(panelId, criteria);
			}
		};		
		tabs.add(customerTab);
		
		ITab projectTab = new AbstractTab(new KeyResourceModel("report.title.project"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getProjectReportPanel(panelId, criteria);
			}
		};		
		tabs.add(projectTab);	
		
		ITab employeeTab = new AbstractTab(new KeyResourceModel("report.title.employee"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getUserReportPanel(panelId, criteria);
			}
		};	
		tabs.add(employeeTab);
		
		return tabs;
	}
	
	private Panel getCustomerReportPanel(String id, ReportCriteria reportCriteria)
	{
		CustomerAggregateReportModel customerAggregateReport = new CustomerAggregateReportModel(reportCriteria);
		ReportUtil.storeInCache(customerAggregateReport);
		return new CustomerReportPanel(id, customerAggregateReport);
	}
	
	private Panel getProjectReportPanel(String id, ReportCriteria reportCriteria)
	{
		ProjectAggregateReportModel aggregateReport = new ProjectAggregateReportModel(reportCriteria);
		ReportUtil.storeInCache(aggregateReport);
		return new ProjectReportPanel(id, aggregateReport);
	}

	private Panel getUserReportPanel(String id, ReportCriteria reportCriteria)
	{
		UserAggregateReportModel aggregateReport = new UserAggregateReportModel(reportCriteria);
		ReportUtil.storeInCache(aggregateReport);
		return new EmployeeReportPanel(id, aggregateReport);
	}
	
}
