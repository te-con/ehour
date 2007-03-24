/**
 * Created on 27-feb-2007
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

package net.rrm.ehour.web.timesheet.action;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectService;
import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.util.DateUtil;
import net.rrm.ehour.web.calendar.CalendarUtil;
import net.rrm.ehour.web.sort.ProjectAssignmentComparator;
import net.rrm.ehour.web.timesheet.dto.PrintReport;
import net.rrm.ehour.web.timesheet.form.PrintTimesheetForm;
import net.rrm.ehour.web.util.AuthUtil;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * TODO 
 **/

public class PrintTimesheetAction extends Action
{
	private	CalendarUtil		calendarUtil;
	private	ReportService		reportService;
	private ProjectService		projectService;
	/**
	 * @throws ParseException 
	 * 
	 */
	public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								HttpServletRequest request,
								HttpServletResponse response) throws ParseException 
	{
		ActionForward			fwd;
		PrintTimesheetForm		psForm = (PrintTimesheetForm)form;
		Integer					userId;
		Calendar				printDate;
		DateRange				printRange;
		
		userId = AuthUtil.getUserId(psForm);
		
		printDate = calendarUtil.getRequestedMonth(request, psForm);
		printRange = DateUtil.getDateRangeForMonth(printDate);
		
		if (psForm != null && psForm.isFromForm())
		{
			fwd = getPrintSheetData(request, printRange, mapping, psForm);
		}
		else
		{
			fwd = getFormData(request, mapping, userId, psForm, printRange);
		}

		request.setAttribute("printDate", printDate);

		return fwd;
	}
	
	/**
	 * Get data for printing sheet and put it on the request
	 * @param request
	 * @param printRange
	 * @param mapping
	 * @param psForm
	 * @return
	 * @throws ParseException 
	 */
	private ActionForward getPrintSheetData(HttpServletRequest request,
											DateRange printRange,
											ActionMapping mapping,
											PrintTimesheetForm psForm) throws ParseException
	{
		List<FlatProjectAssignmentAggregate>	results;
		PrintReport								printReport;
		ActionForward							fwd;
		User									user;
		
		user = AuthUtil.getLoggedInUser();
		
		results = reportService.getPrintReportData(Arrays.asList(psForm.getProjectId()), printRange);
		printReport = new PrintReport();
		printReport.initialize(results);
		
		request.setAttribute("printReport", printReport);
		request.setAttribute("printUser", user);
		request.setAttribute("dateSequence", DateUtil.createDateSequence(printRange));
		request.setAttribute("signatureSpace", psForm.isSignatureSpace());
		request.setAttribute("currentDate", new GregorianCalendar());
		fwd = mapping.findForward("printSheet");
		
		return fwd;
	}
	
	/**
	 * Get data for form
	 * @param request
	 * @param mapping
	 * @param userId
	 * @param psForm
	 * @param printRange
	 * @return
	 */
	
	private ActionForward getFormData(HttpServletRequest request,
										ActionMapping mapping,
										Integer userId,
										PrintTimesheetForm psForm,
										DateRange printRange)
	{
		Set<ProjectAssignment>			projectAssignments;
		SortedSet<ProjectAssignment>	sortedAssignments;
		ActionForward					fwd;
		
		projectAssignments = projectService.getProjectsForUser(userId, printRange);
		sortedAssignments = new TreeSet<ProjectAssignment>(new ProjectAssignmentComparator());
		sortedAssignments.addAll(projectAssignments);
		
		request.setAttribute("projectAssignments", sortedAssignments);
		fwd = mapping.findForward( (psForm.isAjaxCall()) ? "printForm" : "printLayout");

		return fwd;
	}

	/**
	 * @param calendarUtil the calendarUtil to set
	 */
	public void setCalendarUtil(CalendarUtil calendarUtil)
	{
		this.calendarUtil = calendarUtil;
	}

	/**
	 * @param reportService the reportService to set
	 */
	public void setReportService(ReportService reportService)
	{
		this.reportService = reportService;
	}

	/**
	 * @param projectService the projectService to set
	 */
	public void setProjectService(ProjectService projectService)
	{
		this.projectService = projectService;
	}
}
