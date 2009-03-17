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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import ofc4j.model.elements.LineChart;
import ofc4j.model.elements.LineChart.Style;


/**
 * Created on Mar 17, 2009, 5:59:26 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractReportChartFlashBuilder<EL extends ReportElement>
{
	private static final long serialVersionUID = 753705806177534932L;

	
	public LineChart createChartElement(Report report)
	{
		Map<ChartRowKey, Number> valueMap = createChartRowMap(report);

		LineChart chart = new LineChart(Style.DOT);
		
		List<ChartRowKey> keys = new ArrayList<ChartRowKey>(valueMap.keySet());
		
		Collections.sort(keys);
		
		List<Number> data = new ArrayList<Number>();

		for (ChartRowKey rowKeyAgg : keys)
		{
			data.add(valueMap.get(rowKeyAgg));
//			dataset.addValue(valueMap.get(rowKeyAgg), valueAxisLabel, rowKeyAgg);
		}
		
		chart.addValues(data);
		
		return chart;

	}
	
	
	@SuppressWarnings("unchecked")
	private Map<ChartRowKey, Number> createChartRowMap(Report report)
	{
		Map<ChartRowKey, Number> valueMap = new HashMap<ChartRowKey, Number>();
		ChartRowKey		rowKey;
		Number 			value;

		for (ReportElement element : report.getReportData().getReportElements())
		{
			rowKey = getRowKey((EL)element);
			
			value = getColumnValue((EL)element);

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
	
	/**
	 * Get report name
	 * @return
	 */
	protected abstract String getReportNameKey();
	
	/**
	 * Get value axis label
	 * @return
	 */
	protected abstract String getValueAxisLabelKey();
	
	/**
	 * Get row key from report element
	 * @param aggregate
	 * @return
	 */
	protected abstract ChartRowKey getRowKey(EL element);

	/**
	 * Get column value from report element
	 * @param aggregate
	 * @return
	 */
	protected abstract Number getColumnValue(EL element);	
}
