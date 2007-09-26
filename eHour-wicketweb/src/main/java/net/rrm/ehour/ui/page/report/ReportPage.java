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

package net.rrm.ehour.ui.page.report;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.model.KeyResourceModel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.ReportPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Reporting 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class ReportPage extends BaseReportPage
{
	private static final long serialVersionUID = 6614404841734599622L;
	
	private ReportTabbedPanel	tabPanel;

	/**
	 * 
	 */
	public ReportPage()
	{
		super(new ResourceModel("report.title"), null);
		
		final ReportCriteria reportCriteria = getReportCriteria(false);
		final IModel model = new CompoundPropertyModel(new ReportCriteriaBackingBean(reportCriteria));
		setModel(model);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));
		
		List<AbstractTab> tabList = new ArrayList<AbstractTab>();
		
		tabList.add(new AbstractTab(new KeyResourceModel("criteria.title"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return new ReportCriteriaPanel(panelId, model);
			}
		});
		
		tabPanel = new ReportTabbedPanel("reportContainer", tabList);
		tabPanel.setOutputMarkupId(true);
		add(tabPanel);
		
		add(new Label("sidePanel", "dummy"));
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
//		getReportPanel();
		
		List<AbstractTab> tabList = tabPanel.getTabs();
		
		tabList.add(new AbstractTab(new KeyResourceModel("report.title.customer"))
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getReportPanel(panelId);
			}
			
		});
		
		target.addComponent(tabPanel);
		
//		for (AbstractTab tab : tabList)
//		{
//			
//			System.out.println(((KeyResourceModel)tab.getTitle()).getKey());
//		}
	}

	/**
	 * Build report for criteria
	 */
	private Panel getReportPanel(String id)
	{
		ReportCriteria criteria = ((ReportCriteriaBackingBean)getModel().getObject()).getReportCriteria();
		
		// add data
		ReportDataAggregate reportData = getReportData(criteria);
		
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(customerAggregateReport, reportData);
		
		ReportPanel panel = new ReportPanel(id, customerAggregateReport);
		panel.setOutputMarkupId(true);
		
		return panel;
	}
}
