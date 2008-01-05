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
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.reportchart.AbstractChartImage;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.session.EhourWebSession;
import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.model.Model;
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
	/**
	 * 
	 * @param id
	 * @param dataModel
	 * @param width
	 * @param height
	 */
	public AbstractTrendChartImage(String id, Model dataModel, int width, int height)
	{
		super(id, dataModel, width, height);
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
				false, // include legend
				false, // tooltips?
				false // URLs?
				);

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setRangeGridlinePaint(Color.white);
		plot.setBackgroundPaint(new Color(0x536e87));

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
		renderer.setItemLabelFont(rendererTitleFont);
		renderer.setItemLabelPaint(new Color(0xf9f9f9));
		
		// set up gradient paints for series...
		GradientPaint gradientPaint = new GradientPaint(0.0f, 0.0f, new Color(0xbfd9f6), 
														0.0f, 0.0f, new Color(0xa3bcd8));
		renderer.setPaint(gradientPaint);
		
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
		TimeSeries 		dataset;
		Map<ChartRowKey, Number> valueMap = new HashMap<ChartRowKey, Number>();
		ChartRowKey		rowKey;
		Number 			value;
		List<ChartRowKey>	keys;
		
		List<Date> dates = DateUtil.createDateSequence(reportData.getReportCriteria().getReportRange(), config);
		
		dataset = new TimeSeries("test");
		
		for (Date date : dates)
		{
			dataset.add(new Day(date), 0);
		}

		for (ReportElement element : reportData.getReportElements())
		{
			rowKey = getRowKey((EL)element);
			
			value = getColumnValue((EL)element);

			if (value == null)
			{
				value = new Double(0);
			}

			if (valueMap.containsKey(rowKey))
			{
				value = value.doubleValue() + valueMap.get(rowKey).doubleValue();
				valueMap.put(rowKey, value);
			} else
			{
				valueMap.put(rowKey, value);
			}
		}

		keys = new ArrayList<ChartRowKey>(valueMap.keySet());
		
		Collections.sort(keys);
		
		for (ChartRowKey rowKeyDate : keys)
		{
			dataset.addOrUpdate(new Day((Date)rowKeyDate.getName()), valueMap.get(rowKeyDate));
		}

		TimeSeriesCollection collection = new TimeSeriesCollection();
		collection.addSeries(dataset);
		return collection;
	}	

}
