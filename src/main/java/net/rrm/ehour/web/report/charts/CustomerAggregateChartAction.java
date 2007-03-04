/**
 * Created on 3-mar-2007
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

import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class CustomerAggregateChartAction extends AbstractChartAction
{

    /*
     * (non-Javadoc)
     * 
     * @see net.rrm.ehour.web.report.charts.AbstractChartAction#getChart(net.rrm.ehour.report.reports.ReportData)
     */
    @Override
    protected JFreeChart getChart(ReportData reportData, Integer forId)
    {
	logger.debug("Creating customer aggregate chart");
	DefaultCategoryDataset dataset = createDataset(reportData, forId);

	JFreeChart chart = ChartFactory.createBarChart(
		"Customer chart", // chart title
		null, // domain axis label
		"Hours", // range axis label
		dataset, // data
		PlotOrientation.VERTICAL, // orientation
		false, // include legend
		false, // tooltips?
		false // URLs?
		);	
	
	CategoryPlot plot = (CategoryPlot) chart.getPlot();
	plot.setRangeGridlinePaint(Color.white);
	plot.setBackgroundPaint(new Color(0x536e87));
	
	
	BarRenderer renderer = (BarRenderer) plot.getRenderer();
	Font		chartTitleFont = new Font("SansSerif", Font.PLAIN, 10);
	renderer.setBaseItemLabelFont(chartTitleFont);
	renderer.setBaseItemLabelPaint(new Color(0xf9f9f9));
	renderer.setItemLabelFont(chartTitleFont);
	renderer.setItemLabelPaint(new Color(0xf9f9f9));
	
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(
            0.0f, 0.0f, Color.blue, 
            0.0f, 0.0f, new Color(0, 0, 64)
        );
        GradientPaint gp1 = new GradientPaint(
            0.0f, 0.0f, Color.green, 
            0.0f, 0.0f, new Color(0, 64, 0)
        );
        GradientPaint gp2 = new GradientPaint(
            0.0f, 0.0f, Color.red, 
            0.0f, 0.0f, new Color(64, 0, 0)
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);
	
	return chart;
    }

    /**
     * Create dataset of hours per customer
     * @param reportData
     * @return
     */
    private DefaultCategoryDataset createDataset(ReportData reportData, Integer forId)
    {
	DefaultCategoryDataset 	dataset;
	Map<Customer, Number>	valueMap = new HashMap<Customer, Number>();
	Customer		customer;
	Number			value;
	
	dataset = new DefaultCategoryDataset();

	for (ProjectAssignmentAggregate aggregate : reportData.getProjectAssignmentAggregates())
	{
	    customer = aggregate.getProjectAssignment().getProject().getCustomer();
	    
	    if (forId != null && !customer.getCustomerId().equals(forId))
	    {
		continue;
	    }
	    
	    if (valueMap.containsKey(customer))
	    {
		value = valueMap.get(customer).doubleValue() + aggregate.getHours().doubleValue();
		valueMap.put(customer, value);
	    }
	    else
	    {
		valueMap.put(customer, aggregate.getHours());
	    }
	}
	
	for (Customer cust : valueMap.keySet())
	{
	    dataset.addValue(valueMap.get(cust), "hour", cust.getName());
	}

	return dataset;
    }

}
