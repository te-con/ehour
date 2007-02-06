/**
 * Created on 26-jan-2007
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

package net.rrm.ehour.web.userreport.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.web.report.form.ReportCriteriaForm;
import net.rrm.ehour.web.report.util.UserCriteriaAssembler;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class UserReportCriteriaAction extends Action
{
	private ReportCriteria reportCriteria;

	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ReportCriteriaForm	rcForm = (ReportCriteriaForm)form;
		Integer				userId;
		UserCriteria		uc;

		userId = AuthUtil.getUserId(request, rcForm);
		
		uc = UserCriteriaAssembler.getUserCriteria(rcForm);
		uc.setUserIds(new Integer[]{userId});
		uc.setUserFilter(UserCriteria.USER_SINGLE);
		
		// reportCriteria syncs itself when updated
		reportCriteria.setUserCriteria(uc);
		request.setAttribute("criteria", reportCriteria);

		return mapping.findForward("success");
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
