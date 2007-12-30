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

package net.rrm.ehour.ui.reportchart.aggregate;

import org.apache.wicket.model.Model;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.reportchart.rowkey.ProjectRowKey;

/**
 * Hours per project 
 **/

public class ProjectHoursAggregateChartImage extends AbstractAggregateChartImage
{
	private static final long serialVersionUID = 6708618449786705008L;

	/**
	 * 
	 * @param id
	 * @param reportDataAggregate
	 * @param forId
	 * @param width
	 * @param height
	 */
	public ProjectHoursAggregateChartImage(String id, 
											Model dataModel,
											int width,
											int height)
	{
		super(id, dataModel, width, height);	
	}
	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.AbstractAggregateChartAction#getColumnValue(net.rrm.ehour.report.reports.ProjectAssignmentAggregate)
	 */
	@Override
	protected Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getHours();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.AbstractAggregateChartAction#getReportName()
	 */
	@Override
	protected String getReportNameKey()
	{
		return "report.hoursProject";
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.AbstractAggregateChartAction#getRowKey(net.rrm.ehour.report.reports.ProjectAssignmentAggregate)
	 */
	@Override
	protected ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new ProjectRowKey(aggregate.getProjectAssignment().getProject());
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.web.report.charts.AbstractAggregateChartAction#getValueAxisLabel()
	 */
	@Override
	protected String getValueAxisLabelKey()
	{
		return "hours";
	}
}
