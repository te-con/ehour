/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.chart.aggregate;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.report.chart.rowkey.UserRowKey;

/**
 * Turnover per employee 
 **/

public class UserTurnoverAggregateChartDataConverter implements AggregateChartDataConverter
{
	private static final long serialVersionUID = 1L;

	public Number getColumnValue(AssignmentAggregateReportElement aggregate)
	{
		return aggregate.getTurnOver();
	}

	public String getReportNameKey()
	{
		return "report.turnoverEmployee";
	}

	public ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate)
	{
		return new UserRowKey(aggregate.getProjectAssignment().getUser());
	}

	public String getValueAxisLabelKey()
	{
		return "report.turnover";
	}

}
