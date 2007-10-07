/**
 * Created on Jul 10, 2007
 * Created by Thies Edeling
 * Copyright (C) 2007 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.report.user;

import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.page.user.report.UserReportPrint;
import net.rrm.ehour.ui.panel.report.AbstractReportPanel;
import net.rrm.ehour.ui.panel.report.AggregateReportDataPanel;
import net.rrm.ehour.ui.panel.report.ReportType;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.CustomerTurnoverAggregateImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectHoursAggregateChartImage;
import net.rrm.ehour.ui.reportchart.aggregate.ProjectTurnoverAggregateChartImage;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * Report table
 **/

public class UserReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = -2660092982421858132L;
	
	/**
	 * 
	 * @param id
	 * @param reportData
	 */
	public UserReportPanel(String id, CustomerAggregateReport aggregateReport, ReportDataAggregate reportData, boolean inclLinks)
	{
		super(id);
		
		add(getReportPanel(aggregateReport, reportData, inclLinks));
	}
	
	/**
	 * Get report panel
	 * @param customerAggregateReport
	 * @return
	 */
	private WebMarkupContainer getReportPanel(CustomerAggregateReport customerAggregateReport, ReportDataAggregate reportData, boolean inclLinks)
	{
		ResourceLink 	excelLink = null;
		Link			printLink = null;
		
		if (inclLinks)
		{
			final String reportId = customerAggregateReport.getReportId();
			
			ResourceReference excelResource = new ResourceReference("userReportExcel");
			ValueMap params = new ValueMap();
			params.add("reportId", reportId);
			excelLink = new ResourceLink("excelLink", excelResource, params);
			
			printLink = new Link("printLink")
			{
				private static final long serialVersionUID = 7739711270923594216L;

				@Override
				public void onClick()
				{
					setResponsePage(new UserReportPrint(reportId));
				}
			};
			
			printLink.add(new SimpleAttributeModifier("target", "_print"));
			
		}

		// Report model
		StringResourceModel reportTitle = new StringResourceModel("userReport.title", 
																this, null, 
																new Object[]{new DateModel(customerAggregateReport.getReportRange().getDateStart(), config),
																			 new DateModel(customerAggregateReport.getReportRange().getDateEnd(), config)});
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, printLink, excelLink);

		greyBorder.add(new AggregateReportDataPanel("reportTable", customerAggregateReport, ReportType.AGGREGATE_CUSTOMER_SINGLE_USER));
		
		addCharts(reportData, greyBorder);
		
		return greyBorder;
	}
	
	/**
	 * Add charts
	 * @param reportCriteria
	 * @return
	 */
	private void addCharts(ReportDataAggregate data, WebMarkupContainer parent)
	{
		Model dataModel = new Model(data);
		
		// hours per customer
		CustomerHoursAggregateChartImage customerHoursChart = new CustomerHoursAggregateChartImage("customerHoursChart", dataModel, chartWidth, chartHeight);
		parent.add(customerHoursChart);

		// turnover per customer
		if (config.isShowTurnover())
		{
			CustomerTurnoverAggregateImage customerTurnoverChart = new CustomerTurnoverAggregateImage("customerTurnoverChart", dataModel, chartWidth, chartHeight);
			parent.add(customerTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("customerTurnoverChart");
			img.setVisible(false);
			parent.add(img);
		}

		// hours per project
		ProjectHoursAggregateChartImage projectHoursChartFactory = new ProjectHoursAggregateChartImage("projectHoursChart", dataModel, chartWidth, chartHeight);
		parent.add(projectHoursChartFactory);

		// turnover per project
		if (config.isShowTurnover())
		{
			ProjectTurnoverAggregateChartImage projectTurnoverChart = new ProjectTurnoverAggregateChartImage("projectTurnoverChart", dataModel, chartWidth, chartHeight);
			parent.add(projectTurnoverChart);
		}
		else
		{
			// placeholder, not visible anyway
			Image img = new Image("projectTurnoverChart");
			img.setVisible(false);
			parent.add(img);
		}		
	}	

}
