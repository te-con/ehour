/**
 * Created on Sep 27, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.report.panel.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.ui.common.component.AbstractOpenFlashChart;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.ReportDrawType;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerHoursAggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerTurnoverAggregateImage;
import ofc4j.model.Chart;
import ofc4j.model.elements.BarChart;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;

/**
 * Customer report panel
 **/

public class CustomerReportPanel extends AggregateReportPanel
{
	private static final long serialVersionUID = 8422287988040603274L;

	public CustomerReportPanel(String id, TreeReport report)
	{
		this(id, report, ReportDrawType.FLASH);
	}

	public CustomerReportPanel(String id, TreeReport report, ReportDrawType drawType)
	{
		super(id, report, ReportConfig.AGGREGATE_CUSTOMER, "customerReportExcel", drawType);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.type.ReportPanel#addCharts(net.rrm.ehour.report.reports.ReportDataAggregate, org.apache.wicket.markup.html.WebMarkupContainer)
	 */
	@Override
	protected void addCharts(ReportData data, WebMarkupContainer parent)
	{
		Model dataModel = new Model(data);

		// hours per customer
		CustomerHoursAggregateChartImage customerHoursChart = new CustomerHoursAggregateChartImage("hoursChart", dataModel, chartWidth, chartHeight);
		parent.add(customerHoursChart);

		CustomerTurnoverAggregateImage customerTurnoverChart = new CustomerTurnoverAggregateImage("turnoverChart", dataModel, chartWidth, chartHeight);
		parent.add(customerTurnoverChart);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.aggregate.AggregateReportPanel#addFlashCharts(net.rrm.ehour.report.reports.ReportData, org.apache.wicket.markup.html.WebMarkupContainer)
	 */
	@Override
	protected void addFlashCharts(ReportData data, WebMarkupContainer parent)
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

	    parent.add(new AbstractOpenFlashChart("hoursChart", 300,400,chart2));
	    parent.add(new AbstractOpenFlashChart("turnoverChart", 300,400,chart2));
		
	}
}
