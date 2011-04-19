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

package net.rrm.ehour.ui.report.chart.trend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.TreeReportData;
import net.rrm.ehour.ui.report.chart.AbstractReportChartImage;
import net.rrm.ehour.ui.report.chart.UpdatingTimeSeries;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.model.IModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Trend image which uses a timeline
 **/

public abstract class AbstractTrendChartImage<EL extends ReportElement> extends AbstractReportChartImage<EL>
{
	private static final long serialVersionUID = 5240166303490011478L;

	private static final Paint[] seriePaints = new Paint[]{new Color(0xa3bcd8), new Color(0xff6b51), new Color(0xbebd4a), new Color(0x65d460), new Color(0x519fff)};
	
	protected String seriesColumnIndex;
	
	public AbstractTrendChartImage(String id, IModel<ReportData> dataModel, int width, int height, String seriesColumn)
	{
		super(id, dataModel, width, height);
		
		setOutputMarkupId(true);
		
		this.seriesColumnIndex = seriesColumn;
	}
	
	/**
	 * 
	 * @param reportData
	 * @param forId
	 * @param reportName
	 * @return
	 */
	public JFreeChart getChart(ReportData reportData)
	{
		String reportNameKey = getReportNameKey();
		String reportName = getLocalizer().getString(reportNameKey, this);
		EhourConfig config = EhourWebSession.getSession().getEhourConfig();
		
		TimeSeriesCollection dataset = createDataset(reportData, config);

		JFreeChart chart = ChartFactory.createTimeSeriesChart(reportName, // chart title
				"Date", // domain axis label
				getLocalizer().getString(getValueAxisLabelKey(), this), // range axis label
				dataset, // data
				true, // include legend
				false, // tooltips?
				false // URLs?
				);

		XYPlot plot = (XYPlot) chart.getPlot();

		TextTitle	title;
		Font		chartTitleFont = new Font("SansSerif", Font.BOLD, 12);
		
		chart.setBackgroundPaint(new Color(0xf9f9f9));
		chart.setAntiAlias(true);
		title = chart.getTitle();
		title.setFont(chartTitleFont);
		title.setPaint(new Color(0x536e87));
		
		chart.setTitle(title);
		
		XYItemRenderer renderer = (XYItemRenderer) plot.getRenderer();
		Font rendererTitleFont = new Font("SansSerif", Font.PLAIN, 10);
		renderer.setBaseItemLabelFont(rendererTitleFont);
		renderer.setBaseItemLabelPaint(new Color(0xf9f9f9));
		
		// set up gradient paints for series...
		for (int i = 0; i < dataset.getSeriesCount(); i++)
		{
			renderer.setSeriesPaint(i, seriePaints[i % seriePaints.length]);
		}
		
		return chart;
	}	
	
	/**
	 * Create dataset for this chart
	 * @param reportData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private TimeSeriesCollection createDataset(ReportData reportData, EhourConfig config)
	{
		Map<Object, TimeSeries> timeSeries = new HashMap<Object, TimeSeries>();
		
		TreeReportData treeReportData = (TreeReportData)reportData;
		
		ChartRowKey	rowKey;
		Number 		value;
		
		List<Date> dates = DateUtil.createDateSequence(reportData.getReportRange(), config);

		for (ReportElement element : treeReportData.getRawReportData().getReportElements())
		{
			EL castedElement = (EL)element;
			
			TimeSeries timeSerie;
			
			Object seriesKey = getSeriesKey(castedElement);
			
			if (timeSeries.containsKey(seriesKey))
			{
				timeSerie = timeSeries.get(seriesKey);
			}
			else
			{
				timeSerie = new UpdatingTimeSeries(getSeriesName(castedElement));
				nullifyTimeSeries(timeSerie, dates);
				timeSeries.put(seriesKey, timeSerie);
			}
			
			rowKey = getRowKey(castedElement);
			value = getColumnValue(castedElement);

			if (value == null)
			{
				value = new Double(0);
			}
			
			timeSerie.addOrUpdate(new Day((Date) rowKey.getName()), value);
		}

		TimeSeriesCollection collection = new TimeSeriesCollection();

		for (TimeSeries timeSerie : timeSeries.values())
		{
			collection.addSeries(timeSerie);	
		}
		
		
		return collection;
	}
	
	/**
	 * Set all dates in a series to 0
	 * @param series
	 * @param dates
	 */
	private void nullifyTimeSeries(TimeSeries series, List<Date> dates)
	{
		for (Date date : dates)
		{
			series.add(new Day(date), 0);
		}		
	}
	
	/**
	 * Get series key
	 * @return
	 */
	protected abstract Object getSeriesKey(EL element);
	
	/**
	 * Get series name
	 * @param element
	 * @return
	 */
	protected abstract Comparable<?> getSeriesName(EL element);
}
