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

import net.rrm.ehour.web.report.reports.CustomerReport;

import org.apache.struts.action.Action;
import org.jfree.chart.JFreeChart;
/**
 * TODO  fixme
 **/

public class ProjectReportTotalHoursChartAction extends Action//extends AbstractChartAction
{
	/**
	 * Create chart
	 * @param reportData
	 * @return
	 */
	protected JFreeChart getChart(CustomerReport customerReport)
	{
//		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//		
//		Set<Customer>						customers;
//		
//		customers = customerReport.getCustomers();
//		String s = "Hours";
//		
//		for (Customer customer : customers)
//		{
//			dataset.addValue(customerReport.getHourTotal(customer), s, customer.getName());
//		}
//		
//		JFreeChart chart = ChartFactory.createBarChart("Booked hours per customer",
//				"Customer", "Hours", dataset, PlotOrientation.HORIZONTAL,
//				false, false, false);
//		chart.removeLegend();
//		return chart;
		return null;
	}
}
