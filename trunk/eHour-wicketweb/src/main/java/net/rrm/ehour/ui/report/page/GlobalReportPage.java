/**
 * Created on Sep 18, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.report.page;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.cache.CachableObject;
import net.rrm.ehour.ui.common.component.AbstractOpenFlashChart;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReport;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReport;
import net.rrm.ehour.ui.report.panel.aggregate.AggregateReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.CustomerReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.EmployeeReportPanel;
import net.rrm.ehour.ui.report.panel.aggregate.ProjectReportPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.report.panel.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.report.panel.criteria.type.ReportType;
import net.rrm.ehour.ui.report.panel.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.trend.DetailedReport;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Reporting 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class GlobalReportPage extends AbstractReportPage
{
	private static final long serialVersionUID = 6614404841734599622L;
	
	private ReportTabbedPanel	tabPanel;

	/**
	 * 
	 */
	public GlobalReportPage()
	{
		super(new ResourceModel("report.global.title"));
		
		final ReportCriteria reportCriteria = getReportCriteria(false);
		final IModel model = new CompoundPropertyModel(new ReportCriteriaBackingBean(reportCriteria));
		setModel(model);		
		
		List<AbstractTab> tabList = new ArrayList<AbstractTab>();
		
		tabList.add(new AbstractTab(new KeyResourceModel("report.criteria.title"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return new ReportCriteriaPanel(panelId, getModel());
			}
		});
		
		tabPanel = new ReportTabbedPanel("reportContainer", tabList);
		add(tabPanel);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.page.BasePage#ajaxEventReceived(net.rrm.ehour.ui.common.ajax.AjaxEvent)
	 */
	@Override
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == ReportCriteriaAjaxEventType.CRITERIA_UPDATED)
		{
			
			ReportCriteriaBackingBean backingBean = (ReportCriteriaBackingBean)getModel().getObject();
	
			clearTabs();
			
			if (backingBean.getReportType().equals(ReportType.AGGREGATE))
			{
				addAggregateReportPanelTabs	(backingBean);
				
				appendOpenFlashChartJavascript();
			}
			else
			{
				addDetailedReportPanelTabs(backingBean);
			}
			
			ajaxEvent.getTarget().addComponent(tabPanel);
		}
		
		return false;
	}
	
	private void appendOpenFlashChartJavascript()
	{
		List<AbstractOpenFlashChart> charts = CommonWebUtil.findComponent(tabPanel, AbstractOpenFlashChart.class);
		
		if (AjaxRequestTarget.get() != null)
		{
			for (AbstractOpenFlashChart flashChart : charts)
			{
				String javascript = flashChart.getSwf().getJavascript();
		
				AjaxRequestTarget.get().appendJavascript(javascript);
			}
		}
	}
	

	/**
	 * Clear tabs except for the first one
	 */
	@SuppressWarnings("unchecked")
	private void clearTabs()
	{
		List<AbstractTab> tabs = tabPanel.getTabs();
		
		while (tabs.size() > 1)
		{
			tabs.remove(1);
		}
	}
	
	/**
	 * Get the report panel
	 */
	private void addAggregateReportPanelTabs(ReportCriteriaBackingBean backingBean)
	{
		final ReportCriteria criteria = backingBean.getReportCriteria();
		
		ITab	customerTab = new AbstractTab(new KeyResourceModel("report.title.customer"))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Panel getPanel(String panelId)
			{
				return getCustomerReportPanel(panelId, criteria);
			}
		};		
		tabPanel.addTab(customerTab);
		
		ITab	projectTab = new AbstractTab(new KeyResourceModel("report.title.project"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getProjectReportPanel(panelId, criteria);
			}
		};		
		tabPanel.addTab(projectTab);	
		
		ITab	employeeTab = new AbstractTab(new KeyResourceModel("report.title.employee"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getUserReportPanel(panelId, criteria);
			}
		};	
		tabPanel.addTab(employeeTab);
		
		tabPanel.setSelectedTab(1);
	}
	
	/**
	 * Get the report panel
	 */
	private void addDetailedReportPanelTabs(ReportCriteriaBackingBean backingBean)
	{
		final ReportCriteria criteria = backingBean.getReportCriteria();
		
		ITab	detailedTab = new AbstractTab(new KeyResourceModel("report.title.detailed"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getDetailedReportPanel(panelId, criteria);
			}
		};			
		tabPanel.addTab(detailedTab);	
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
		storeInCache(customerAggregateReport);
		AggregateReportPanel panel = new CustomerReportPanel(id, customerAggregateReport);
		
		return panel;
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
		storeInCache(aggregateReport);
		AggregateReportPanel panel = new ProjectReportPanel(id, aggregateReport);
		
		return panel;
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
		storeInCache(aggregateReport);
		AggregateReportPanel panel = new EmployeeReportPanel(id, aggregateReport);
		
		return panel;
	}
	
	/**
	 * Get detailed report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getDetailedReportPanel(String id, ReportCriteria reportCriteria)
	{
		DetailedReport detailedReport = new DetailedReport(reportCriteria, this.getConfig().getLocale());
		storeInCache(detailedReport);
		DetailedReportPanel panel = new DetailedReportPanel(id, detailedReport);
		
		return panel;
	}	

	/**
	 * keep the report available for excel reporting in a later request
	 * @param report
	 */
	private void storeInCache(CachableObject report)
	{
		EhourWebSession.getSession().getReportCache().addObjectToCache(report);
	}
}
