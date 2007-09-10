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

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.reportchart.rowkey.CustomerRowKey;

public class CustomerHoursAggregateChartAction extends AbstractAggregateChart
{
	// TODO i18n
	@Override
	protected String getReportName()
	{
		return "Hours per customer";
	}

	/**
	 * Get the hours from the aggregate
	 */
	@Override
	protected Number getColumnValue(ProjectAssignmentAggregate aggregate)
	{
		return aggregate.getHours();
	}

	/**
	 * Get the customerrowkey decorator from the aggregate
	 */
	@Override
	protected ChartRowKey getRowKey(ProjectAssignmentAggregate aggregate)
	{
		return new CustomerRowKey(aggregate.getProjectAssignment().getProject().getCustomer());
	}

	/**
	 * TODO: i18n
	 */
	@Override
	protected String getValueAxisLabel()
	{
		return "Hours";
	}
}
