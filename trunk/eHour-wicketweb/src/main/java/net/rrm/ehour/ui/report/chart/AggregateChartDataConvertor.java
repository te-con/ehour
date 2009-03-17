/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.report.chart;

import java.io.Serializable;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;

/**
 * Created on Mar 17, 2009, 6:30:38 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public interface AggregateChartDataConvertor extends Serializable
{
	public String getReportNameKey();
	
	public Number getColumnValue(AssignmentAggregateReportElement aggregate);
	
	public ChartRowKey getRowKey(AssignmentAggregateReportElement aggregate);
	
	public String getValueAxisLabelKey();
}
