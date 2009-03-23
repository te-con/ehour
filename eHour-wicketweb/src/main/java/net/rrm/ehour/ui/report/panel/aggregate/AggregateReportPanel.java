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
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.OpenFlashChart;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.ReportDrawType;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.flash.HorizontalChartBuilder;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;

import ofc4j.model.Chart;
import ofc4j.model.elements.BarChart;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Fragment;

/**
 * Full report panel containing report data and the charts 
 **/
public abstract class AggregateReportPanel extends AbstractReportPanel
{
	private static final int CHART_WIDTH = 460;

	private static final long serialVersionUID = 2173644826934093029L;

	public AggregateReportPanel(String id, TreeReport report,
									ReportConfig reportConfig, String excelResourceName,
									ReportDrawType drawType)
	{
		super(id, CHART_WIDTH);

		setOutputMarkupId(true);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);

		greyBorder.add(new TreeReportDataPanel("reportTable", report, reportConfig, excelResourceName, getReportWidth() - 50));
		
		ReportData reportData = report.getReportData();
		
		if (drawType == ReportDrawType.IMAGE)
		{
			addImageCharts(reportData, greyBorder);
		}
		else
		{
			addOpenFlashCharts(reportData, greyBorder);
		}
	}
	
	private void addOpenFlashCharts(ReportData data, WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("charts", "flash", this);
		addFlashCharts("hoursChart", "turnoverChart", data, fragment);
		parent.add(fragment);
	}
	
	private void addImageCharts(ReportData data, WebMarkupContainer parent)
	{
		Fragment fragment = new Fragment("charts", "image", this);
		addCharts("hoursChart", "turnoverChart", data, fragment);
		parent.add(fragment);
	}
	
	/**
	 * Add image charts
	 * @param reportCriteria
	 * @return
	 */
	protected void addCharts(String hourId, String turnoverId, ReportData data, WebMarkupContainer parent)
	{
		
	}
	
	/**
	 * Add image charts. 
	 * @param reportCriteria
	 * @return
	 */
	protected void addFlashCharts(String hourId, String turnoverId, ReportData data, WebMarkupContainer parent)
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

	    parent.add(new OpenFlashChart(hourId, 300,400,chart2));
	    parent.add(new OpenFlashChart(turnoverId, 300,400,chart2));
	}
	

	protected OpenFlashChart createHorizontalFlashChart(String id, ReportData data, AggregateChartDataConverter converter)
	{
		Chart chart = new HorizontalChartBuilder().buildChart(data, converter);
		return new OpenFlashChart(id, getChartWidth(), getChartHeight(), chart);
	}
}
