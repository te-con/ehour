/**
 * Created on Jan 3, 2008
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

package net.rrm.ehour.ui.reportchart.detailed;

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
import net.rrm.ehour.ui.reportchart.AbstractChartImage;
import net.rrm.ehour.ui.reportchart.UpdatingTimeSeries;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.session.EhourWebSession;
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

public abstract class AbstractTrendChartImage<EL extends ReportElement> extends AbstractChartImage<EL>
{
	private static final Paint[] seriePaints = new Paint[]{new Color(0xa3bcd8), new Color(0xff6b51), new Color(0xbebd4a), new Color(0x65d460), new Color(0x519fff)};
	
	protected String seriesColumnIndex;
	
	/**
	 * 
	 * @param id
	 * @param dataModel
	 * @param width
	 * @param height
	 */
	public AbstractTrendChartImage(String id, IModel dataModel, int width, int height, String seriesColumn)
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
	public JFreeChart getChart(ReportData<EL> reportData)
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
	private TimeSeriesCollection createDataset(ReportData<EL> reportData, EhourConfig config)
	{
		Map<Object, TimeSeries> timeSeries = new HashMap<Object, TimeSeries>();
		
		ChartRowKey			rowKey;
		Number 					value;
		
		// 
		List<Date> dates = DateUtil.createDateSequence(reportData.getReportCriteria().getReportRange(), config);

		//
		for (ReportElement element : reportData.getReportElements())
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
