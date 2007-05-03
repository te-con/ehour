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
package net.rrm.ehour.web.report.charts.aggregate;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.web.report.charts.rowkey.ChartRowKey;
import net.rrm.ehour.web.report.charts.rowkey.CustomerRowKey;

public class CustomerHoursAggregateChartAction extends AbstractAggregateChartAction
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
