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

package net.rrm.ehour.ui.report.panel.user;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AbstractOpenFlashChart;
import net.rrm.ehour.ui.common.model.DateModel;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.util.CommonWebUtil;
import net.rrm.ehour.ui.report.aggregate.CustomerAggregateReport;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerTurnoverAggregateImage;
import net.rrm.ehour.ui.report.chart.aggregate.ProjectHoursAggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.ProjectTurnoverAggregateChartImage;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;
import net.rrm.ehour.ui.report.user.page.UserReportPrint;
import ofc4j.model.Chart;
import ofc4j.model.elements.BarChart;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Fragment;
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
	public UserReportPanel(String id, CustomerAggregateReport aggregateReport, ReportData<AssignmentAggregateReportElement> reportData, boolean inclLinks)
	{
		super(id, -1, 730);
		
		add(getReportPanel(aggregateReport, reportData, inclLinks));
	}
	
	/**
	 * Get report panel
	 * @param customerAggregateReport
	 * @return
	 */
	private WebMarkupContainer getReportPanel(CustomerAggregateReport customerAggregateReport, ReportData<AssignmentAggregateReportElement> reportData, boolean inclLinks)
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
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("reportFrame", reportTitle, true, printLink, excelLink, CommonWebUtil.GREYFRAME_WIDTH);

		greyBorder.add(new TreeReportDataPanel("reportTable", customerAggregateReport, ReportConfig.AGGREGATE_CUSTOMER_SINGLE_USER, null, getReportWidth() - 30));
		
		Fragment frag = new Fragment("charts", "flash", this);
		greyBorder.add(frag);
		
		addFlashCharts(reportData, frag);
		
		return greyBorder;
	}
	
	
	/**
	 * Add charts
	 * @param reportCriteria
	 * @return
	 */
	private void addFlashCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
	{
	    BarChart bar1 = new BarChart(BarChart.Style.GLASS);
	    bar1.setColour("#007FFF");
	    bar1.setTooltip("Beers:<br>Value:#val#");
	    bar1.addValues(1,5,8,3,0,2);
	    bar1.setText("Beers consumed");
	    bar1.setAlpha(0.1f);

	    BarChart bar2 = new BarChart(BarChart.Style.GLASS);
	    bar2.setColour("#802A2A");
	    bar2.setTooltip("#val#<br>bugs fixed");
	    bar2.setText("bugs fixed");
	    bar2.setFontSize(15);
	    bar1.setAlpha(0.9f);
	    bar2.addValues(2,7,1,5,8,3,0,2);

	    Chart chart2 = new Chart("Beers and bugs");
	    chart2.addElements(bar1,bar2);
	    chart2.setBackgroundColour("#FFFFFF");

	    parent.add(new AbstractOpenFlashChart("customerHoursChart", 300,400,chart2));
		
	}	
	
	
	/**
	 * Add jfree charts
	 * @param reportCriteria
	 * @return
	 */
	private void addImageCharts(ReportData<AssignmentAggregateReportElement> data, WebMarkupContainer parent)
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
