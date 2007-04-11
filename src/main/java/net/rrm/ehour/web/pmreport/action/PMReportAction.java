/**
 * Created on Apr 11, 2007
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

package net.rrm.ehour.web.pmreport.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.reports.ProjectManagerReport;
import net.rrm.ehour.web.pmreport.form.PMReportForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * 
 **/

public class PMReportAction extends BasePMReportAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		PMReportForm			pmReportForm = (PMReportForm)form;
		ProjectManagerReport	pmReport;
		DateRange				reportRange;
		
		reportRange = new DateRange(pmReportForm.getDateStartAsDate(),
									pmReportForm.getDateEndAsDate());
		
		pmReport = reportService.getProjectManagerReport(reportRange, pmReportForm.getProjectId());
		
		request.setAttribute("pmReport", pmReport);
		
		return mapping.findForward("success");
	}

}
