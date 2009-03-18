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
import net.rrm.ehour.ui.report.TreeReportData;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConvertor;
import net.rrm.ehour.ui.report.chart.ReportChartFlashBuilder;
import net.rrm.ehour.ui.report.chart.aggregate.AggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerHoursAggregateChartDataConvertor;
import net.rrm.ehour.ui.report.chart.aggregate.CustomerTurnoverAggregateChartDataConvertor;
import ofc4j.model.Chart;
import ofc4j.model.elements.HorizontalBarChart;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
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
		AggregateChartDataConvertor hourProvider = new CustomerHoursAggregateChartDataConvertor();
		Image customerHoursChart = new AggregateChartImage("hoursChart", dataModel, chartWidth, chartHeight, hourProvider);
		parent.add(customerHoursChart);

		AggregateChartDataConvertor turnoverProvider = new CustomerTurnoverAggregateChartDataConvertor();
		Image customerTurnoverChart = new AggregateChartImage("turnoverChart", dataModel, chartWidth, chartHeight, turnoverProvider);
		parent.add(customerTurnoverChart);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.aggregate.AggregateReportPanel#addFlashCharts(net.rrm.ehour.report.reports.ReportData, org.apache.wicket.markup.html.WebMarkupContainer)
	 */
	@Override
	protected void addFlashCharts(ReportData data, WebMarkupContainer parent)
	{
		ReportData rawData = ((TreeReportData)data).getRawReportData();
		AggregateChartDataConvertor hourConvertor = new CustomerHoursAggregateChartDataConvertor();

		HorizontalBarChart chart = ReportChartFlashBuilder.createChartElement(rawData, hourConvertor);
	    
		chart.setColour("#007FFF");
		chart.setTooltip("Beers:<br>Value:#val#");
		chart.setText("Beers consumed");

	    Chart chart2 = new Chart("Beers and bugs");
	    chart2.addElements(chart);
	    chart2.setBackgroundColour("#FFFFFF");

	    parent.add(new AbstractOpenFlashChart("hoursChart", 500,300,chart2));
	    parent.add(new AbstractOpenFlashChart("turnoverChart", 50,50 ,chart2));
	}
}
