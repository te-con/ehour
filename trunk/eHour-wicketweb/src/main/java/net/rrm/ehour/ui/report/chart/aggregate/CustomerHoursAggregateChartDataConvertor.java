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
package net.rrm.ehour.ui.report.chart.aggregate;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConvertor;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.report.chart.rowkey.CustomerRowKey;

/**
 * Factory for hours per customer chart
 * @author Thies
 *
 */
public class CustomerHoursAggregateChartDataConvertor implements AggregateChartDataConvertor
{
	private static final long serialVersionUID = 8323295032556266163L;
	
	public String getReportNameKey()
	{
		return "report.hoursCustomer";
	}

	public Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getHours();
	}

	public ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new CustomerRowKey(aggregate.getProjectAssignment().getProject().getCustomer());
	}

	public String getValueAxisLabelKey()
	{
		return "general.hours";
	}
}
