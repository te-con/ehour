/**
 * Created on Mar 4, 2007
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

package net.rrm.ehour.ui.reportchart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;

import org.apache.log4j.Logger;
import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebResponse;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Base class for charts 
 **/

public abstract class AbstractChartImage<EL extends ReportElement> extends NonCachingImage
{
	private	final static Logger	logger = Logger.getLogger(AbstractChartImage.class);

	private int		width;
	private int		height;
	
	/**
	 * 
	 * @param id
	 * @param dataModel
	 * @param width
	 * @param height
	 */
	public AbstractChartImage(String id, 
								IModel dataModel,
								int width,
								int height)
	{
		super(id, dataModel);

		this.width = width;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.wicket.markup.html.image.Image#getImageResource()
	 */
	@Override
	@SuppressWarnings({"serial", "unchecked"})
	protected Resource getImageResource()
	{
		return new DynamicImageResource()
		{
			@Override
			protected byte[] getImageData()
			{
				ReportData<EL> reportData = (ReportData<EL>)getModelObject();
				JFreeChart chart = getChart(reportData);
				return toImageData(chart.createBufferedImage(width, height));
			}

			@Override
			protected void setHeaders(WebResponse response)
			{
				if (isCacheable())
				{
					super.setHeaders(response);
				} else
				{
					response.setHeader("Pragma", "no-cache");
					response.setHeader("Cache-Control", "no-cache");
					response.setDateHeader("Expires", 0);
				}
			}
		};
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
		logger.debug("Creating " + reportName + " chart");
		
		DefaultCategoryDataset dataset = createDataset(reportData);

		JFreeChart chart = ChartFactory.createBarChart(reportName, // chart title
				null, // domain axis label
				getLocalizer().getString(getValueAxisLabelKey(), this), // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				false, // include legend
				false, // tooltips?
				false // URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		//	plot.setRangeGridlinePaint(Color.white);
		//	plot.setBackgroundPaint(new Color(0x536e87));

		TextTitle	title;
		Font		chartTitleFont = new Font("SansSerif", Font.BOLD, 12);
		
		chart.setBackgroundPaint(new Color(0xf9f9f9));
		chart.setAntiAlias(true);
		title = chart.getTitle();
		title.setFont(chartTitleFont);
		title.setPaint(new Color(0x536e87));
		
		chart.setTitle(title);
		
		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		Font rendererTitleFont = new Font("SansSerif", Font.PLAIN, 10);
		renderer.setBaseItemLabelFont(rendererTitleFont);
		renderer.setBaseItemLabelPaint(new Color(0xf9f9f9));
	
		// set up gradient paints for series...
		GradientPaint gradientPaint = new GradientPaint(0.0f, 0.0f, new Color(0xbfd9f6), 
														0.0f, 0.0f, new Color(0xa3bcd8));
		renderer.setSeriesPaint(0, gradientPaint);
		
		return chart;
	}
	
	/**
	 * Create dataset for this chart
	 * @param reportData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DefaultCategoryDataset createDataset(ReportData<EL> reportData)
	{
		DefaultCategoryDataset dataset;
		Map<ChartRowKey, Number> valueMap = new HashMap<ChartRowKey, Number>();
		ChartRowKey		rowKey;
		Number 			value;
		String			valueAxisLabel = getValueAxisLabelKey();
		List<ChartRowKey>	keys;

		dataset = new DefaultCategoryDataset();

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
		
		for (ChartRowKey rowKeyAgg : keys)
		{
			dataset.addValue(valueMap.get(rowKeyAgg), valueAxisLabel, rowKeyAgg);
		}

		return dataset;
	}
	

	/**
	 * Get report name
	 * @return
	 */
	protected abstract String getReportNameKey();
	
	/**
	 * Get value axis label
	 * @return
	 */
	protected abstract String getValueAxisLabelKey();
	
	/**
	 * Get row key from report element
	 * @param aggregate
	 * @return
	 */
	protected abstract ChartRowKey getRowKey(EL element);

	/**
	 * Get column value from report element
	 * @param aggregate
	 * @return
	 */
	protected abstract Number getColumnValue(EL element);

	/**
	 * @return the width
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight()
	{
		return height;
	}
}
