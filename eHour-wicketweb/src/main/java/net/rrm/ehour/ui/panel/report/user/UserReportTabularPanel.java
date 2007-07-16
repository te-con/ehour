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

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.FloatModel;
import net.rrm.ehour.ui.panel.timesheet.TimesheetPanel;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportNode;
import net.rrm.ehour.ui.report.reports.aggregate.CustomerReport;
import net.rrm.ehour.ui.report.reports.aggregate.AggregateReportNode.SectionChild;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.PropertyModel;

/**
 * Report table
 **/

public class UserReportTabularPanel extends Panel
{
	private static final long serialVersionUID = -2740688272163704885L;

//	private	ReportDataAggregate	reportData;
	private final EhourConfig	config;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	public UserReportTabularPanel(String id, ReportDataAggregate reportData)
	{
		super(id);
		
		config = EhourWebSession.getSession().getEhourConfig();
		
//		this.reportData = reportData;
		
		CustomerReport	customerReport = new CustomerReport();
		customerReport.initialize(reportData);
		
		ListView report = new ListView("report", new PropertyModel(customerReport, "reportNodes"))
		{
			@Override
			protected void populateItem(ListItem item)
			{
				item.add(new CustomerBlock("customerList", (AggregateReportNode<?, ?>)item.getModelObject()));
			}			
		};

		// TODO set title
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", "Report");
		add(greyBorder);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		greyBorder.add(blueBorder);
		blueBorder.add(report);
		
		add(new StyleSheetReference("reportStyle", new CompressedResourceReference(UserReportTabularPanel.class, "style/reportStyle.css")));		
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	private class CustomerBlock extends ListView
	{
		private static final long serialVersionUID = -1589085260912518831L;
		private final AggregateReportNode<?, ?>	node;
		
		public CustomerBlock(String id, AggregateReportNode<?, ?> node)
		{
			super(id, node.getChildNodes());
			this.node = node;
		}

		@Override
		protected void populateItem(ListItem item)
		{
			SectionChild	child = (SectionChild)item.getModelObject();
			
			item.add(new Label("customer", node.getRootValue().getName()));
			item.add(new Label("project", child.getChildValue().getName()));
			item.add(new Label("hours", new FloatModel(child.getHours(), config)));
		}
	}
}
