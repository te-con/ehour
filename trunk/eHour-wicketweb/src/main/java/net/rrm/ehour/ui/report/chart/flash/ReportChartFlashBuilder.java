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

package net.rrm.ehour.ui.report.chart.flash;

import java.util.HashMap;
import java.util.Map;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import ofc4j.model.Chart;

/**
 * Created on Mar 17, 2009, 5:59:26 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class ReportChartFlashBuilder
{
	private static final long serialVersionUID = 753705806177534932L;

	/**
	 * Build chart 
	 * @param reportData
	 * @param dataConvertor
	 * @return
	 */
	public final Chart buildChart(ReportData reportData, AggregateChartDataConverter dataConvertor)
	{
		Chart chartContainer = new Chart(dataConvertor.getReportNameKey());
		
		Map<ChartRowKey, Number> rowMap = createChartRowMap(reportData, dataConvertor);
		
		build(rowMap, chartContainer);
		
		return chartContainer;
		
	}
	
	protected abstract void build(Map<ChartRowKey, Number> valueMap, Chart chartContainer);
	
	protected Map<ChartRowKey, Number> createChartRowMap(ReportData reportData, AggregateChartDataConverter dataConvertor)
	{
		Map<ChartRowKey, Number> valueMap = new HashMap<ChartRowKey, Number>();
		ChartRowKey		rowKey;
		Number 			value;

		for (ReportElement element : reportData.getReportElements())
		{
			rowKey = dataConvertor.getRowKey((AssignmentAggregateReportElement)element);
			
			value = dataConvertor.getColumnValue((AssignmentAggregateReportElement)element);

			if (value == null)
			{
				value = new Double(0);
			}

			if (valueMap.containsKey(rowKey))
			{
				value = value.doubleValue() + valueMap.get(rowKey).doubleValue();
				valueMap.put(rowKey, value);
			} else
			{
				valueMap.put(rowKey, value);
			}
		}
		
		return valueMap;
	}	
}
