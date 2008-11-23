/**
 * Created on Dec 31, 2007
 * Author: Thies
 *
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

package net.rrm.ehour.ui.report.panel.detail;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.report.chart.detailed.AbstractTrendChartImage;
import net.rrm.ehour.ui.report.chart.detailed.DateHoursTrendImage;
import net.rrm.ehour.ui.report.chart.detailed.SeriesChartSelector;
import net.rrm.ehour.ui.report.chart.detailed.TrendChartImageFactory;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.ReportConfig;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Detailed report
 **/

public class DetailedReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = 1L;
	
	public DetailedReportPanel(String id, 
									final TreeReport<FlatReportElement> reportData, 
									final ReportData<FlatReportElement> data)
	{
		super(id);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);
		
		greyBorder.add(new TreeReportDataPanel("reportTable", reportData, ReportConfig.DETAILED_REPORT, "detailedReportExcel", getReportWidth() - 50));
		
		DateHoursTrendImageFactory chartFactory = new DateHoursTrendImageFactory();

		// hours per customer
		AbstractTrendChartImage<FlatReportElement> chart = chartFactory.getTrendChartImage("userReport.report.project", new Model(data));
		greyBorder.add(chart);	
		
		greyBorder.add(new SeriesChartSelector<FlatReportElement>("serieChartSelector", ReportConfig.DETAILED_REPORT, chart, chartFactory));
	}

	/**
	 * 
	 * @author Thies
	 *
	 */
	class DateHoursTrendImageFactory implements TrendChartImageFactory<FlatReportElement>
	{
		private static final long serialVersionUID = 1L;

		public AbstractTrendChartImage<FlatReportElement> getTrendChartImage(String seriesColumn, IModel model)
		{
			return  new DateHoursTrendImage("hoursChart", model, 920, chartHeight, seriesColumn);
		}
		
	}
}
