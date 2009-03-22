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

package net.rrm.ehour.ui.report.chart.aggregate;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.report.chart.rowkey.ProjectRowKey;

/**
 * Turnover per project
 **/

public class ProjectTurnoverAggregateChartDataConverter implements AggregateChartDataConverter
{
	private static final long serialVersionUID = 6171422114780586475L;

	public Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getTurnOver();
	}

	public String getReportNameKey()
	{
		return "report.turnoverProject";
	}

	public ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new ProjectRowKey(aggregate.getProjectAssignment().getProject());
	}

	public String getValueAxisLabelKey()
	{
		return "report.turnover";
	}

}
