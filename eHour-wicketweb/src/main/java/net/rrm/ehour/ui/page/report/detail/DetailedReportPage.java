/**
 * Created on Dec 29, 2007
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

package net.rrm.ehour.ui.page.report.detail;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.model.KeyResourceModel;
import net.rrm.ehour.ui.page.report.BaseReportPage;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.nav.report.ReportNavPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaBackingBean;
import net.rrm.ehour.ui.panel.report.criteria.ReportCriteriaPanel;
import net.rrm.ehour.ui.panel.report.criteria.ReportTabbedPanel;
import net.rrm.ehour.ui.panel.report.detail.DetailedReportPanel;
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
 * Detailed report 
 **/

@AuthorizeInstantiation("ROLE_REPORT")
public class DetailedReportPage extends BaseReportPage
{
	private static final long serialVersionUID = 187757929348342350L;
	
	@SpringBean
	private ReportService		reportService;
	private ReportTabbedPanel	tabPanel;

	/**
	 * 
	 */
	public DetailedReportPage()
	{
		super(new ResourceModel("report.title"));
		
		add(new ReportNavPanel("reportNav"));
		add(new ContextualHelpPanel("contextHelp"));
		
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

	/**
	 * Get report data
	 * @param reportCriteria
	 * @return
	 */
	protected ReportData getReportData(ReportCriteria reportCriteria)
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
	 * Get the report panel
	 */
	private void addReportPanelTabs()
	{
		ReportCriteria criteria = ((ReportCriteriaBackingBean)getModel().getObject()).getReportCriteria();
		
		final ReportData reportData = getReportData(criteria);
		
		ITab detailedTab = new AbstractTab(new KeyResourceModel("report.title.detailed"))
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			public Panel getPanel(String panelId)
			{
				return getDetailedReportPanel(panelId, reportData);
			}
		};		
		tabPanel.addTab(detailedTab);
		tabPanel.setSelectedTab(1);
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
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		addReportPanelTabs();
		target.addComponent(tabPanel);
	}	
}
