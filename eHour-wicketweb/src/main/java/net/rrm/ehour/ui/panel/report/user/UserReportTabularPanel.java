/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.panel.report.user;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.report.reports.ReportDataProvider;
import net.rrm.ehour.ui.report.reports.aggregate.CustomerReport;

import org.apache.wicket.markup.html.panel.Panel;

/**
 * Report table
 **/

public class UserReportTabularPanel extends Panel
{
	private static final long serialVersionUID = -2740688272163704885L;

	private	ReportDataAggregate	reportData;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	public UserReportTabularPanel(String id, ReportDataAggregate reportData)
	{
		super(id);
		
		this.reportData = reportData;
		
		CustomerReport	customerReport = new CustomerReport();
		customerReport.initialize(reportData);
		
		add(new UserReportDataView("report", new ReportDataProvider(customerReport)));
//		ListView report = new ListView("report", new PropertyModel(customerReport, "topNodes"))
//		{
//			@Override
//			protected void populateItem(ListItem item)
//			{
//				System.out.println(item.getModelObject());
//				item.add(new Label("test", item.getModelObject().toString()));
////				final Customer	customer = (Customer)item.getModelObject();
////
////				// check for any preference
////				CustomerFoldPreference foldPreference = timesheet.getFoldPreferences().get(customer);
////				
////				if (foldPreference == null)
////				{
////					foldPreference = new CustomerFoldPreference(timesheet.getUser(), customer, false);
////				}
////				
////				item.add(getCustomerLabel(customer, foldPreference));
//////				item.add(new Label("customerDesc", customer.getDescription()));
////
////				boolean hidden = (foldPreference != null && foldPreference.isFolded());
////				
////				item.add(new TimesheetRowList("rows", timesheet.getCustomers().get(customer), hidden, grandTotals, form));
//			}			
//		};
		
//		add(report);
	}
}
