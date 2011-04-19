package net.rrm.ehour.ui.report.page.command;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReport;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReport;
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
	
	/**
	 * Get customer report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getCustomerReportPanel(String id, ReportCriteria reportCriteria)
	{
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportCriteria);
		ReportUtil.storeInCache(customerAggregateReport);
		return new CustomerReportPanel(id, customerAggregateReport);
	}
	
	/**
	 * Get project report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getProjectReportPanel(String id, ReportCriteria reportCriteria)
	{
		ProjectAggregateReport	aggregateReport = new ProjectAggregateReport(reportCriteria);
		ReportUtil.storeInCache(aggregateReport);
		return new ProjectReportPanel(id, aggregateReport);
	}
	
	/**
	 * Get user report panel
	 * 
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getUserReportPanel(String id, ReportCriteria reportCriteria)
	{
		UserAggregateReport	aggregateReport = new UserAggregateReport(reportCriteria);
		ReportUtil.storeInCache(aggregateReport);
		return new EmployeeReportPanel(id, aggregateReport);
	}
	
}
