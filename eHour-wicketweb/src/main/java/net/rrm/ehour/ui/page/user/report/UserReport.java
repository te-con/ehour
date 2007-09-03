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

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.page.user.report.criteria.UserReportCriteriaPanel;
import net.rrm.ehour.ui.panel.contexthelp.ContextualHelpPanel;
import net.rrm.ehour.ui.panel.report.user.UserReportTabularPanel;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.CompoundPropertyModel;
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
	
	/**
	 * 
	 */
	public UserReport()
	{
		super("reporting", null);
		
		ReportCriteria reportCriteria = getReportCriteria();
		setModel(new CompoundPropertyModel(reportCriteria));
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", reportCriteria));
		
		// 
		ReportDataAggregate reportData = getReportData(reportCriteria);
		add(new UserReportTabularPanel("reportTable", reportData));
		
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
