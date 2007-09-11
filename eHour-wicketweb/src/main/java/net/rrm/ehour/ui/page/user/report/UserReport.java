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
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.page.user.report.criteria.UserReportCriteriaPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.user.UserReportPanel;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerTurnoverAggregateImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectTurnoverAggregateChartImage;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
		setModel(new CompoundPropertyModel(reportCriteria));
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", reportCriteria));
		
		// add data
		ReportDataAggregate reportData = getReportData(reportCriteria);
		add(new UserReportPanel("reportTable", reportData));
		
		// add charts
		addCharts(reportData, null);
	}

	/**
	 * Add charts
	 * @param reportCriteria
	 * @return
	 */
	private void addCharts(ReportDataAggregate data, Integer forId)
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("chartContainer");
		add(greyBorder);
		
		// TODO cache the model
		Model dataModel = new Model(data);
		
		// hours per customer
		CustomerHoursAggregateChartImage customerHoursChart = new CustomerHoursAggregateChartImage("customerHoursChart", dataModel, forId, chartWidth, chartHeight);
		greyBorder.add(customerHoursChart);

		// turnover per customer
		if (config.isShowTurnover())
		{
			CustomerTurnoverAggregateImage customerTurnoverChart = new CustomerTurnoverAggregateImage("customerTurnoverChart", dataModel, forId, chartWidth, chartHeight);
			greyBorder.add(customerTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("customerTurnoverChart");
			img.setVisible(false);
			greyBorder.add(img);
		}

		// hours per project
		ProjectHoursAggregateChartImage projectHoursChartFactory = new ProjectHoursAggregateChartImage("projectHoursChart", dataModel, forId, chartWidth, chartHeight);
		greyBorder.add(projectHoursChartFactory);

		// turnover per project
		if (config.isShowTurnover())
		{
			ProjectTurnoverAggregateChartImage projectTurnoverChart = new ProjectTurnoverAggregateChartImage("projectTurnoverChart", dataModel, forId, chartWidth, chartHeight);
			greyBorder.add(projectTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("projectTurnoverChart");
			img.setVisible(false);
			greyBorder.add(img);
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
			initUserCritieria();
		}
		
		userCriteria.setSingleUser(true);
		
		return reportCriteriaService.getReportCriteria(userCriteria);
	}
	
	/**
	 * Initialize user criteria 
	 */
	private void initUserCritieria()
	{
		userCriteria = new UserCriteria();
		userCriteria.setSingleUser(true);
		userCriteria.setUser(EhourWebSession.getSession().getUser().getUser());
		
		userCriteria.setReportRange(DateUtil.getDateRangeForMonth(DateUtil.getCalendar(EhourWebSession.getSession().getEhourConfig())));
	}
}
