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

package net.rrm.ehour.web.report.action;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.report.project.ProjectAssignmentAggregate;
import net.rrm.ehour.report.project.ProjectReport;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * TODO 
 **/

public class ProjectChartReportAction extends Action
{
	private	Logger			logger = Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String			sessionKey;
		HttpSession		session;
		ProjectReport	report;
		JFreeChart		chart;
		
		session = request.getSession();
		sessionKey = request.getParameter("key");

		report = (ProjectReport)session.getAttribute(sessionKey);
		
		chart = getChart(report);

		response.setContentType("image/jpeg");
		ChartUtilities.writeChartAsJPEG(response.getOutputStream(), 0.95f, chart, 400, 300);
		return null;
	}
	
	/**
	 * Create chart
	 * @param report
	 * @return
	 */
	private JFreeChart getChart(ProjectReport report)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		Set<Customer>						customers;
		List<ProjectAssignmentAggregate>	aggregates;
		customers = report.getCustomers();
		
		for (Customer customer : customers)
		{
			aggregates = report.getReportValues().get(customer);
			
			for (ProjectAssignmentAggregate aggregate : aggregates)
			{
				dataset.setValue(aggregate.getHours(), "Hours", customer.getName());
			}
		}
		
		JFreeChart chart = ChartFactory.createBarChart("Hours",
				"Customer", "Hours", dataset, PlotOrientation.VERTICAL,
				false, true, false);
		
		return chart;
	}
}
