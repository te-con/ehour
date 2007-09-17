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
import net.rrm.ehour.ui.panel.report.user.UserReportPanel;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
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
	private WebMarkupContainer		reportDataPanel;
	
	/**
	 * 
	 */
	public UserReport()
	{
		super("reporting", null);
		
		ReportCriteria reportCriteria = getReportCriteria();
		IModel	model = new CompoundPropertyModel(reportCriteria);
		setModel(model);
		
		// contextual help
		add(new ContextualHelpPanel("contextHelp"));

		// add criteria
		add(new UserReportCriteriaPanel("sidePanel", model));

		// add data
		reportDataPanel = getReport();
		add(reportDataPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.page.BasePage#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	@Override
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		WebMarkupContainer	replacement = getReport();
		reportDataPanel.replaceWith(replacement);
		reportDataPanel = replacement;
		target.addComponent(replacement);
	}
	
	/**
	 * Get report
	 * @return
	 */
	private WebMarkupContainer getReport()
	{
		ReportCriteria criteria = (ReportCriteria)getModel().getObject();
		
		// add data
		ReportDataAggregate reportData = getReportData(criteria);
		CustomerAggregateReport	customerAggregateReport = new CustomerAggregateReport(reportData);
		((EhourWebSession)(getSession())).getReportCache().addReportToCache(customerAggregateReport, reportData);
		
		UserReportPanel panel = new UserReportPanel("userReportPanel", customerAggregateReport, reportData, true);
		panel.setOutputMarkupId(true);
		return panel;
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
