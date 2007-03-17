/**
 * Created on Mar 4, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.web.report.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.HashMap;
import java.util.Map;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.charts.rowkey.ChartRowKey;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * TODO 
 **/

public abstract class AbstractAggregateChartAction extends AbstractChartAction
{
	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.AbstractChartAction#getChart(net.rrm.ehour.report.reports.ReportData, java.lang.Integer)
	 */
	@Override
	protected JFreeChart getChart(ReportData reportData, Integer forId)
	{
		logger.debug("Creating " + getReportName() + " aggregate chart");
		
		DefaultCategoryDataset dataset = createDataset(reportData, forId);

		JFreeChart chart = ChartFactory.createBarChart(getReportName(), // chart title
				null, // domain axis label
				getValueAxisLabel(), // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				false, // include legend
				false, // tooltips?
				false // URLs?
				);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		//	plot.setRangeGridlinePaint(Color.white);
		//	plot.setBackgroundPaint(new Color(0x536e87));

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		Font chartTitleFont = new Font("SansSerif", Font.PLAIN, 10);
		renderer.setBaseItemLabelFont(chartTitleFont);
		renderer.setBaseItemLabelPaint(new Color(0xf9f9f9));
		renderer.setItemLabelFont(chartTitleFont);
		renderer.setItemLabelPaint(new Color(0xf9f9f9));

		// set up gradient paints for series...
		GradientPaint gradientPaint = new GradientPaint(0.0f, 0.0f, new Color(0xbfd9f6), 0.0f, 0.0f, new Color(0xa3bcd8));
		renderer.setPaint(gradientPaint);
		
		return chart;
	}
	
	/**
	 * Create dataset for this aggregate chart
	 * @param reportData
	 * @return
	 */
	private DefaultCategoryDataset createDataset(ReportData reportData, Integer forId)
	{
		DefaultCategoryDataset dataset;
		Map<String, Number> valueMap = new HashMap<String, Number>();
		ChartRowKey		rowKey;
		Number 	value;
		String	valueAxisLabel = getValueAxisLabel();

		dataset = new DefaultCategoryDataset();

		for (ProjectAssignmentAggregate aggregate : reportData.getProjectAssignmentAggregates())
		{
			rowKey = getRowKey(aggregate);
			
			value = getColumnValue(aggregate);
			
			if (forId != null && !rowKey.getId().equals(forId))
			{
				System.out.println("skipping");
				continue;
			}

			if (value == null)
			{
				value = new Double(0);
			}


			if (valueMap.containsKey(rowKey.getName()))
			{
				value = value.doubleValue() + valueMap.get(rowKey.getName()).doubleValue();
				valueMap.put(rowKey.getName(), value);
			} else
			{
				valueMap.put(rowKey.getName(), value);
			}
		}

		for (String rowKeyAgg : valueMap.keySet())
		{
			dataset.addValue(valueMap.get(rowKeyAgg), valueAxisLabel, rowKeyAgg);
		}

		return dataset;
	}
	

	/**
	 * Get report name
	 * @return
	 */
	protected abstract String getReportName();
	
	/**
	 * Get value axis label
	 * @return
	 */
	protected abstract String getValueAxisLabel();
	
	/**
	 * Get row key from aggregate
	 * @param aggregate
	 * @return
	 */
	protected abstract ChartRowKey getRowKey(ProjectAssignmentAggregate aggregate);

	/**
	 * Get column value from aggregate
	 * @param aggregate
	 * @return
	 */
	protected abstract Number getColumnValue(ProjectAssignmentAggregate aggregate);
}
