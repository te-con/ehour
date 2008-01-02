/**
 * Created on 3-mar-2007
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
package net.rrm.ehour.ui.reportchart.aggregate;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.reportchart.AbstractChartImage;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.reportchart.rowkey.CustomerRowKey;

import org.apache.wicket.model.Model;

/**
 * Factory for hours per customer chart
 * @author Thies
 *
 */
public class CustomerHoursAggregateChartImage extends AbstractChartImage<AssignmentAggregateReportElement>
{
	private static final long serialVersionUID = 8323295032556266163L;

	/**
	 * 
	 * @param id
	 * @param reportDataAggregate
	 * @param forId
	 * @param width
	 * @param height
	 */
	public CustomerHoursAggregateChartImage(String id, 
													Model dataModel,
													int width,
													int height)
	{
		super(id, dataModel, width, height);	
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.aggregate.AbstractAggregateChartFactory#getReportNameKey()
	 */
	@Override
	protected String getReportNameKey()
	{
		return "report.hoursCustomer";
	}

	/**
	 * Get the hours from the aggregate
	 */
	@Override
	protected Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getHours();
	}

	/**
	 * Get the customerrowkey decorator from the aggregate
	 */
	@Override
	protected ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new CustomerRowKey(aggregate.getProjectAssignment().getProject().getCustomer());
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.aggregate.AbstractAggregateChartFactory#getValueAxisLabel()
	 */
	@Override
	protected String getValueAxisLabelKey()
	{
		return "hours";
	}
}
