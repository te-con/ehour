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
 */

package net.rrm.ehour.ui.report.chart.aggregate;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.report.chart.rowkey.ProjectRowKey;

/**
 * Hours per project 
 **/

public class ProjectHoursAggregateChartDataConverter implements AggregateChartDataConverter
{
	private static final long serialVersionUID = 6708618449786705008L;

	public Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getHours();
	}

	public String getReportNameKey()
	{
		return "report.hoursProject";
	}

	public ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new ProjectRowKey(aggregate.getProjectAssignment().getProject());
	}

	public String getValueAxisLabelKey()
	{
		return "general.hours";
	}
}
