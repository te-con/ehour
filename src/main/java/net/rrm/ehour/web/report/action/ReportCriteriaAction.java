/**
 * Created on Jan 14, 2007
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
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;
import net.rrm.ehour.web.report.util.UserCriteriaAssembler;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO
 */

public class ReportCriteriaAction extends Action
{
	private ReportCriteria 	reportCriteria;
	private	Logger			logger = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ReportCriteriaForm 	criteriaForm = (ReportCriteriaForm)form;
		ActionForward		fwd;
		
		updateCriteria(criteriaForm);
		
		if (criteriaForm != null)
		{
			switch (criteriaForm.getUpdateType())
			{
				case ReportCriteria.UPDATE_CUSTOMERS:
					fwd = mapping.findForward("customers");
					break;
				case ReportCriteria.UPDATE_PROJECTS:
					fwd = mapping.findForward("projects");
					break;
				case ReportCriteria.UPDATE_USERS:
					fwd = mapping.findForward("users");
					break;
				default:
					fwd = mapping.findForward("success");
					break;
			}
		}
		else
		{
			fwd = mapping.findForward("success");
		}
		
		request.setAttribute("criteria", reportCriteria);
		
		response.setHeader("Cache-Control", "no-cache");
		return fwd;
	}

	/**
	 * Update criteria
	 * @param criteriaForm
	 */
	private void updateCriteria(ReportCriteriaForm criteriaForm)
	{
		UserCriteria	uc;
		int				updateType;
		
		logger.debug("Updating UserCriteria");
		
		if (criteriaForm != null)
		{
			try
			{
				uc = UserCriteriaAssembler.getUserCriteria(criteriaForm);
				updateType = criteriaForm.getUpdateType();
			} catch (ParseException e)
			{
				logger.error("Invalid date format specified when creating report", e);
				uc = new UserCriteria();
				updateType = ReportCriteria.UPDATE_ALL;
			}
		}
		else
		{
			uc = new UserCriteria();
			updateType = ReportCriteria.UPDATE_ALL;
		}
			

		reportCriteria.setUserCriteria(uc);
 		reportCriteria.updateAvailableCriteria(updateType);
	}
	
	/**
	 * @param availReportCriteria
	 *            the availReportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}
}
