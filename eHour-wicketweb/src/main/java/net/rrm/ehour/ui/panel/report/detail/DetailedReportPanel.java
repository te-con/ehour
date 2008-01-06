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

package net.rrm.ehour.ui.panel.report.detail;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.panel.report.AbstractReportPanel;
import net.rrm.ehour.ui.panel.report.ReportConfig;
import net.rrm.ehour.ui.panel.report.TreeReportDataPanel;
import net.rrm.ehour.ui.report.TreeReport;
import net.rrm.ehour.ui.reportchart.detailed.AbstractTrendChartImage;
import net.rrm.ehour.ui.reportchart.detailed.DateHoursTrendImage;
import net.rrm.ehour.ui.reportchart.detailed.SeriesChartSelector;
import net.rrm.ehour.ui.reportchart.detailed.TrendChartImageFactory;
import net.rrm.ehour.ui.util.CommonUIStaticData;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Detailed report
 **/

public class DetailedReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = 1L;
	
	public DetailedReportPanel(String id, TreeReport reportData, ReportData data)
	{
		super(id);
		
		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", CommonUIStaticData.GREYFRAME_WIDTH);
		add(greyBorder);
		
		greyBorder.add(new TreeReportDataPanel("reportTable", reportData, ReportConfig.DETAILED_REPORT, "detailedReportExcel"));
		
		Model dataModel = new Model(data);
		
		DateHoursTrendImageFactory chartFactory = new DateHoursTrendImageFactory();

		// hours per customer
		AbstractTrendChartImage<FlatReportElement> chart = chartFactory.getTrendChartImage(0, dataModel);
		greyBorder.add(chart);	
		
		greyBorder.add(new SeriesChartSelector<FlatReportElement>("serieChartSelector", ReportConfig.DETAILED_REPORT, chart, chartFactory));
	}
	
	class DateHoursTrendImageFactory implements TrendChartImageFactory<FlatReportElement>
	{

		public AbstractTrendChartImage<FlatReportElement> getTrendChartImage(int seriesColumn, IModel model)
		{
			return  new DateHoursTrendImage("hoursChart", model, 700, chartHeight, seriesColumn);
		}
		
	}
}
