/**
 * Created on Sep 28, 2007
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
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.ReportDrawType;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.TreeReportData;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.AggregateChartImage;
import net.rrm.ehour.ui.report.chart.aggregate.UserHoursAggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.aggregate.UserTurnoverAggregateChartDataConverter;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.Model;

public class EmployeeReportPanel extends AggregateReportPanel
{
	private static final long serialVersionUID = 2594554714722639450L;

	public EmployeeReportPanel(String id, TreeReport report)
	{
		this(id, report, ReportDrawType.IMAGE);
	}
	
	public EmployeeReportPanel(String id, TreeReport report, ReportDrawType drawType)
	{
		super(id, report, ReportConfig.AGGREGATE_EMPLOYEE, EmployeeReportExcel.getId(), drawType);
	}	

	@Override
	protected void addCharts(String hourId, String turnOverId, ReportData data, WebMarkupContainer parent)
	{
		ReportData rawData = ((TreeReportData)data).getRawReportData();
		Model dataModel = new Model(rawData);
		
		AggregateChartDataConverter hourConverter = new UserHoursAggregateChartDataConverter();
		Image customerHoursChart = new AggregateChartImage(hourId, dataModel, getChartWidth(), getChartHeight(), hourConverter);
		parent.add(customerHoursChart);

		AggregateChartDataConverter turnoverConverter = new UserTurnoverAggregateChartDataConverter();
		Image customerTurnoverChart = new AggregateChartImage(turnOverId, dataModel, getChartWidth(), getChartHeight(), turnoverConverter);
		parent.add(customerTurnoverChart);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.panel.aggregate.AggregateReportPanel#addFlashCharts(net.rrm.ehour.report.reports.ReportData, org.apache.wicket.markup.html.WebMarkupContainer)
	 */
	@Override
	protected void addFlashCharts(String hourId, String turnOverId, ReportData data, WebMarkupContainer parent)
	{
		ReportData rawData = ((TreeReportData)data).getRawReportData();
		
		parent.add(createHorizontalFlashChart(hourId, rawData, new UserHoursAggregateChartDataConverter()));
		parent.add(createHorizontalFlashChart(turnOverId, rawData, new UserTurnoverAggregateChartDataConverter()));
		
	}
}
