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

package net.rrm.ehour.ui.page.report.global;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.model.KeyResourceModel;
import net.rrm.ehour.ui.page.report.BaseReportPage;
import net.rrm.ehour.ui.panel.report.aggregate.AggregateReportPanel;
import net.rrm.ehour.ui.panel.report.aggregate.CustomerReportPanel;
import net.rrm.ehour.ui.panel.report.aggregate.EmployeeReportPanel;
import net.rrm.ehour.ui.panel.report.aggregate.ProjectReportPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.panel.report.detail.DetailedReportPanel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.aggregate.ProjectAggregateReport;
import net.rrm.ehour.ui.report.aggregate.UserAggregateReport;
import net.rrm.ehour.ui.report.trend.DetailedReport;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Reporting 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class AggregatedReportPage extends BaseReportPage
{
	private static final long serialVersionUID = 6614404841734599622L;
	
	@SpringBean
	private ReportService		reportService;
	private ReportTabbedPanel	tabPanel;

	/**
	 * 
	 */
	public AggregatedReportPage()
	{
		super(new ResourceModel("report.title"));
		
		final ReportCriteria reportCriteria = getReportCriteria(false);
		final IModel model = new CompoundPropertyModel(new ReportCriteriaBackingBean(reportCriteria));
		setModel(model);		
		
		List<AbstractTab> tabList = new ArrayList<AbstractTab>();
		
		tabList.add(new AbstractTab(new KeyResourceModel("criteria.title"))
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
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		ReportCriteriaBackingBean backingBean = (ReportCriteriaBackingBean)getModel().getObject();

		if (backingBean.getReportType() == 0)
		{
			addAggregateReportPanelTabs	(backingBean);
		}
		else
		{
			addDetailedReportPanelTabs(backingBean);
		}
		
		target.addComponent(tabPanel);
	}

	/**
	 * Get the report panel
	 */
	private void addAggregateReportPanelTabs(ReportCriteriaBackingBean backingBean)
	{
		ReportCriteria criteria = backingBean.getReportCriteria();
		
		final ReportData reportData = getReportData(criteria);
		
		ITab	customerTab = new AbstractTab(new KeyResourceModel("report.title.customer"))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Panel getPanel(String panelId)
			{
				return getCustomerReportPanel(panelId, reportData);
			}
		};		
		tabPanel.addTab(customerTab);
		
		ITab	projectTab = new AbstractTab(new KeyResourceModel("report.title.project"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getProjectReportPanel(panelId, reportData);
			}
		};		
		tabPanel.addTab(projectTab);	
		
		ITab	employeeTab = new AbstractTab(new KeyResourceModel("report.title.employee"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getUserReportPanel(panelId, reportData);
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
		ReportCriteria criteria = backingBean.getReportCriteria();
		
		final ReportData reportData = getDetailedReportData(criteria);	

		ITab	detailedTab = new AbstractTab(new KeyResourceModel("report.title.detailed"))
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Panel getPanel(String panelId)
			{
				return getDetailedReportPanel(panelId, reportData);
			}
		};			
		tabPanel.addTab(detailedTab);	
	}
	
	/**
	 * Get report data
	 * @param reportCriteria
	 * @return
	 */
	protected ReportData getDetailedReportData(ReportCriteria reportCriteria)
	{
		List<FlatReportElement> data;
		
		if (reportCriteria.getUserCriteria().getProjects().isEmpty())
		{
			data = reportService.getReportData(reportCriteria.getUserCriteria().getCustomer(), 
												reportCriteria.getUserCriteria().getReportRange());
		}
		else
		{
			
			data = reportService.getReportData((Project[])reportCriteria.getUserCriteria().getProjects().toArray(new Project[reportCriteria.getUserCriteria().getProjects().size()]), 
													reportCriteria.getUserCriteria().getReportRange());
		}
		
		ReportData reportData = new ReportData();
		reportData.setReportElements(data);
		reportData.setReportCriteria(reportCriteria);
		return reportData;
	}	
	
	
	/**
	 * Get customer report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getCustomerReportPanel(String id, ReportData reportData)
	{
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(customerAggregateReport, reportData);
		
		AggregateReportPanel panel = new CustomerReportPanel(id, customerAggregateReport, reportData);
		panel.setOutputMarkupId(true);
		
		return panel;
	}
	
	/**
	 * Get project report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getProjectReportPanel(String id, ReportData reportData)
	{
		ProjectAggregateReport	aggregateReport = new ProjectAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(aggregateReport, reportData);
		
		AggregateReportPanel panel = new ProjectReportPanel(id, aggregateReport, reportData);
		panel.setOutputMarkupId(true);
		
		return panel;
	}	
	
	/**
	 * Get user report panel
	 * 
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getUserReportPanel(String id, ReportData reportData)
	{
		UserAggregateReport	aggregateReport = new UserAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(aggregateReport, reportData);
		
		AggregateReportPanel panel = new EmployeeReportPanel(id, aggregateReport, reportData);
		panel.setOutputMarkupId(true);
		
		return panel;
	}
	
	/**
	 * Get detailed report panel
	 * @param id
	 * @param reportData
	 * @return
	 */
	private Panel getDetailedReportPanel(String id, ReportData reportData)
	{
		DetailedReport detailedReport = new DetailedReport(reportData, this.getConfig().getLocale());
		
		// for excel reporting
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(detailedReport, reportData);
		
		DetailedReportPanel panel = new DetailedReportPanel(id, detailedReport, reportData);
		panel.setOutputMarkupId(true);
		
		return panel;
	}		
}
