/**
 * Created on 1-feb-2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.report.action;

import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.AggregateReport;
import net.rrm.ehour.web.report.reports.AggregateReportFactory;
import net.rrm.ehour.web.report.util.UserCriteriaAssembler;
import net.rrm.ehour.web.util.AuthUtil;
import net.rrm.ehour.web.util.WebConstants;

import org.apache.struts.action.ActionMapping;

/**
 * Creates new report data and new aggregated report
 **/

public class CreateReportAction extends AbstractCreateAggregateReportAction
{
	private ReportCriteria 	reportCriteria;
	private	ReportService	reportService;

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.action.AbstractCreateAggregateReportAction#getAggregateReport(java.lang.String, java.lang.Integer, net.rrm.ehour.report.reports.ReportData)
	 */
	@Override
	protected AggregateReport getAggregateReport(HttpSession session,
												String reportName,
												ReportForm reportForm,
												ReportData reportData)
	{
		AggregateReport	report;
		Integer			forId;
		
		forId = reportForm.getForId();
		
		logger.debug("Creating new " + reportName + " aggregate report");
		
		if (forId == null || forId.intValue() == 0)
		{
			report = AggregateReportFactory.createReport(reportName, reportData);
		}
		else
		{
			report = AggregateReportFactory.createReport(reportName, reportData, forId);
		}
		
		return report;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.web.report.action.AbstractCreateAggregateReportAction#getReportData(javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionMapping, net.rrm.ehour.web.report.form.ReportForm)
	 */
	@Override
	protected ReportData getReportData(HttpServletRequest request, ActionMapping mapping, ReportForm form)
	{
		String				param;
		ReportCriteriaForm	rcForm = (ReportCriteriaForm)form;
		UserCriteria		uc;
		ReportData			reportData;
		
		param = mapping.getParameter();
		
		uc = getUserCriteria(param, rcForm);
		
		reportCriteria.setUserCriteria(uc);
		
		logger.debug("Getting new report data for criteria " + uc);
		
		reportData = reportService.createReportData(reportCriteria);
		
		return reportData;
	}	

	/**
	 * Get the user criteria from the form and check authorization
	 * @param param
	 * @param rcForm
	 * @return
	 * @throws ParseException
	 */
	private UserCriteria getUserCriteria(String param, ReportCriteriaForm rcForm)
	{
		UserCriteria 	uc;
		User			loggedInUser;
		
		uc = UserCriteriaAssembler.getUserCriteria(rcForm);
		
		if (param.equals("report") &&
			AuthUtil.hasRole(WebConstants.ROLE_REPORT))
		{
			uc.setSingleUser(false);
		}
		else
		{
			loggedInUser = AuthUtil.getLoggedInUser();
			uc.setUserIds(new Integer[]{loggedInUser.getUserId()});	
			uc.setSingleUser(true);
			
			if (!(param.equals("consultant")))
			{
				logger.warn(loggedInUser + " tried to access overall reporting without proper privileges !");
			}
		}		
		
		return uc;
	}
	
	/**
	 * Session data should be replaced
	 */
	@Override
	protected boolean isReplaceSessionData()
	{
		return true;
	}
	
	/**
	 * @param reportCriteria the reportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

}
