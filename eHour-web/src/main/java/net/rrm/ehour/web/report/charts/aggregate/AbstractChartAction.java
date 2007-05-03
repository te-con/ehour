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

package net.rrm.ehour.web.report.charts.aggregate;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.web.report.action.ReUseReportAction;
import net.rrm.ehour.web.report.form.ReportChartForm;
import net.rrm.ehour.web.report.form.ReportForm;
import net.rrm.ehour.web.report.reports.aggregate.AggregateReport;
import net.rrm.ehour.web.report.util.ChartUtil;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
/**
 * Base class for charts, fetches report data and chart sizes and 
 * leaves chart generation to impl 
 **/

public abstract class AbstractChartAction extends ReUseReportAction
{
	/**
	 * Not too clean..
	 */
	protected AggregateReport getAggregateReport(HttpSession session,
			 String reportName,
			 ReportForm reportForm,
			 ReportDataAggregate reportDataAggregate)
	{
		return null;
	}
	
	/**
	 * 
	 */
	protected ActionForward processReport(ActionMapping mapping,
											HttpServletResponse response,
											ReportForm reportForm,
											String reportName,
											ReportDataAggregate reportDataAggregate,
											AggregateReport report)
	{
		ReportChartForm chartForm = (ReportChartForm)reportForm;
		JFreeChart		chart;
		int				chartWidth;
		int				chartHeight;
		Integer			forId;

		logger.debug("Creating chart for report " + reportName);
		response.setContentType("image/png");

		chartWidth = (chartForm.getChartWidth() == 0) ? 250 : chartForm.getChartWidth();
		chartHeight = (chartForm.getChartHeight() == 0) ? 120 : chartForm.getChartHeight();
		
		forId = chartForm.getForId();
		
		if (forId == 0)
		{
		    forId = null;
		}
		
		chart = getChart(reportDataAggregate, forId);
		ChartUtil.changeChartStyle(chart);
				
		try
		{
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, chartWidth, chartHeight);
		} catch (IOException e)
		{
			logger.error("Can't write chart to outputstream: " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Session data shouldn't be replaced as other charts on the same page
	 * could be created with the same session key
	 */
	protected boolean isReplaceSessionData()
	{
		return false;
	}
	
	/**
	 * Create chart
	 * @param reportDataAggregate
	 * @return
	 */
	protected abstract JFreeChart getChart(ReportDataAggregate reportDataAggregate, Integer forId);
}
