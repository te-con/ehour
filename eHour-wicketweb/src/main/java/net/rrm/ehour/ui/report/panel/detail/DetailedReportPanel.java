/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.detail;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.report.chart.trend.AbstractTrendChartImage;
import net.rrm.ehour.ui.report.chart.trend.DateHoursTrendImage;
import net.rrm.ehour.ui.report.chart.trend.SeriesChartSelector;
import net.rrm.ehour.ui.report.chart.trend.TrendChartImageFactory;
import net.rrm.ehour.ui.report.panel.AbstractReportPanel;
import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;
import net.rrm.ehour.ui.report.trend.DetailedReport;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Detailed report
 **/

public class DetailedReportPanel extends AbstractReportPanel
{
	private static final long serialVersionUID = 1L;

	public DetailedReportPanel(String id, final DetailedReport report)
	{
		super(id);

		GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder("reportFrame", getReportWidth());
		add(greyBorder);

		greyBorder.add(new TreeReportDataPanel("reportTable", report, ReportConfig.DETAILED_REPORT, DetailedReportExcel.getId(), getReportWidth().getValue() - 50));

		DateHoursTrendImageFactory chartFactory = new DateHoursTrendImageFactory();

		ReportData rawData = report.getReportData();

		// hours per customer
		@SuppressWarnings({ "unchecked", "rawtypes" })
		AbstractTrendChartImage<FlatReportElement> chart = chartFactory.getTrendChartImage("userReport.report.project", new Model(rawData));
		greyBorder.add(chart);

		greyBorder.add(new SeriesChartSelector<FlatReportElement>("serieChartSelector", ReportConfig.DETAILED_REPORT, chart, chartFactory));
	}

	@SuppressWarnings("serial")
	private class DateHoursTrendImageFactory implements TrendChartImageFactory<FlatReportElement>
	{
		@SuppressWarnings("unchecked")
		public AbstractTrendChartImage<FlatReportElement> getTrendChartImage(String seriesColumn, @SuppressWarnings("rawtypes") IModel model)
		{
			return new DateHoursTrendImage("hoursChart", model, 920, getChartHeight().getValue(), seriesColumn);
		}
	}
}
