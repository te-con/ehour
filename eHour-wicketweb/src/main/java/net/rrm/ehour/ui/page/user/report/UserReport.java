/**
 * Created on Jul 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.page.user.report;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.page.user.report.criteria.UserReportCriteriaPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.user.UserReportPanel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerTurnoverAggregateImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectTurnoverAggregateChartImage;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;

/**
 * Reporting for user
 **/

@AuthorizeInstantiation("ROLE_CONSULTANT")
public class UserReport extends BasePage
{
	private static final long serialVersionUID = -8867366237264687482L;
	
	@SpringBean
	private ReportCriteriaService	reportCriteriaService;
	@SpringBean
	private ReportService			reportService;
	private	UserCriteria			userCriteria;
	private transient Logger		logger = Logger.getLogger(UserReport.class);
	private EhourConfig				config;
	private int						chartWidth;
	private int						chartHeight;
	private WebMarkupContainer		reportDataPanel;
	
	/**
	 * 
	 */
	public UserReport()
	{
		super("reporting", null);
	
		config = getEhourConfig();

		chartWidth = !config.isShowTurnover() ? 600 : 350;
		chartHeight = 200;
		
		ReportCriteria reportCriteria = getReportCriteria();
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model));

		// add data
		reportDataPanel = getReportPanel();
		add(reportDataPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		WebMarkupContainer	replacement = getReportPanel();
		reportDataPanel.replaceWith(replacement);
		reportDataPanel = replacement;
		target.addComponent(replacement);
	}
	
	/**
	 * Get report panel
	 * @return
	 */
	private WebMarkupContainer getReportPanel()
	{
		ReportCriteria criteria = (ReportCriteria)getModel().getObject();
		
		// add data
		ReportDataAggregate reportData = getReportData(criteria);
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(customerAggregateReport);

		ResourceReference excelResource = new ResourceReference("userReportExcel");
		ValueMap params = new ValueMap();
		params.add("reportId", customerAggregateReport.getReportId());
		ResourceLink excelLink = new ResourceLink("excelLink", excelResource, params);

		// Report model
		StringResourceModel reportTitle = new StringResourceModel("userReport.title", 
																this, null, 
																new Object[]{new DateModel(criteria.getReportRange().getDateStart(), config),
																			 new DateModel(criteria.getReportRange().getDateEnd(), config)});
		
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, true, null, excelLink);
		greyBorder.setOutputMarkupId(true);

		greyBorder.add(new UserReportPanel("reportTable", customerAggregateReport));
		
		addCharts(reportData, greyBorder);
		
		return greyBorder;
	}

	/**
	 * Add charts
	 * @param reportCriteria
	 * @return
	 */
	private void addCharts(ReportDataAggregate data, WebMarkupContainer parent)
	{
		Model dataModel = new Model(data);
		
		// hours per customer
		CustomerHoursAggregateChartImage customerHoursChart = new CustomerHoursAggregateChartImage("customerHoursChart", dataModel, chartWidth, chartHeight);
		parent.add(customerHoursChart);

		// turnover per customer
		if (config.isShowTurnover())
		{
			CustomerTurnoverAggregateImage customerTurnoverChart = new CustomerTurnoverAggregateImage("customerTurnoverChart", dataModel, chartWidth, chartHeight);
			parent.add(customerTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("customerTurnoverChart");
			img.setVisible(false);
			parent.add(img);
		}

		// hours per project
		ProjectHoursAggregateChartImage projectHoursChartFactory = new ProjectHoursAggregateChartImage("projectHoursChart", dataModel, chartWidth, chartHeight);
		parent.add(projectHoursChartFactory);

		// turnover per project
		if (config.isShowTurnover())
		{
			ProjectTurnoverAggregateChartImage projectTurnoverChart = new ProjectTurnoverAggregateChartImage("projectTurnoverChart", dataModel, chartWidth, chartHeight);
			parent.add(projectTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("projectTurnoverChart");
			img.setVisible(false);
			parent.add(img);
		}		
	}

	
	/**
	 * Get report data
	 * @param reportCriteria
	 * @return
	 */
	private ReportDataAggregate getReportData(ReportCriteria reportCriteria)
	{
		logger.debug("Getting report data");
		ReportDataAggregate data = reportService.createAggregateReportData(reportCriteria);
		
		return data;
	}
	
	/**
	 * Get report criteria
	 * @return
	 */
	private ReportCriteria getReportCriteria()
	{
		userCriteria = EhourWebSession.getSession().getUserCriteria();
		
		if (userCriteria == null)
		{
			initUserCriteria();
			EhourWebSession.getSession().setUserCriteria(userCriteria);
		}
		
		userCriteria.setSingleUser(true);
		
		return reportCriteriaService.getReportCriteria(userCriteria);
	}
	
	/**
	 * Initialize user criteria 
	 */
	private void initUserCriteria()
	{
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		
		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
	}
}
