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

package net.rrm.ehour.ui.report.trend;

import static org.springframework.util.Assert.notNull;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.common.report.AbstractCachableReportModel;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Base trend report
 **/

public abstract class TrendReport<RK extends Comparable<?>> extends AbstractCachableReportModel
{
	private static final long serialVersionUID = -8062083697181324496L;
	
	private static final Logger LOGGER = Logger.getLogger(TrendReport.class);
	private transient SortedMap<RK, Map<Date, FlatReportElement>> rowMap;
	
	/**
	 * @param criteria
	 */
	public TrendReport(ReportCriteria criteria)
	{
		super(criteria);
	}
	
	/*
	 * 3(non-Javadoc)
	 * @see net.rrm.ehour.ui.common.report.AbstractCachableReportModel#getReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	@Override
	protected ReportData getReportData(ReportCriteria reportCriteria)
	{
		Map<Date, FlatReportElement>	rowAggregates;
		Date	aggregateDate;
		RK		rowKey;
		
		ReportData aggregateData = getValidReportData(reportCriteria);
		
		rowMap = new TreeMap<RK, Map<Date, FlatReportElement>>(getRKComparator());

		for (ReportElement element : aggregateData.getReportElements())
		{
			FlatReportElement aggregate = (FlatReportElement)element;
			
			rowKey = getRowKey(aggregate);
			
			if (rowMap.containsKey(rowKey))
			{
				rowAggregates = rowMap.get(rowKey);
			}
			else
			{
				rowAggregates = new HashMap<Date, FlatReportElement>();
			}
			
			aggregateDate = getValidAggregateDate(aggregate);
			aggregateDate = DateUtil.nullifyTime(aggregateDate);
			
			rowAggregates.put(aggregateDate, aggregate);
			
			rowMap.put(rowKey, rowAggregates);
		}
		
		return aggregateData;
	}
	
	private ReportData getValidReportData(ReportCriteria reportCriteria)
	{
		ReportData reportData = fetchReportData(reportCriteria);
		
		notNull(reportData);
		notNull(reportData.getReportElements());

		return reportData;
	}
	
    protected abstract ReportData fetchReportData(ReportCriteria reportCriteria);
 
	/**
	 * Get grand total hours
	 * @return
	 */
	public float getGrandTotalHours()
	{
		float total = 0;
		Map<Date, FlatReportElement>	aggMap;
		
		for (RK key : rowMap.keySet())
		{
			aggMap = rowMap.get(key);
			
			for (Date date : aggMap.keySet())
			{
				if (aggMap.containsKey(date))
				{
					Number n = aggMap.get(date).getTotalHours();
					
					if (n != null)
					{
						total+= n.floatValue();
					}
				}
			}
		}
		
		return total;
	}
	
	/**
	 * Get the values
	 * @return
	 */
	public SortedMap<RK, Map<Date, FlatReportElement>> getValues()
	{
		if (rowMap == null)
		{
			lazyInitRowMap();
		}
		return rowMap;
	}
	
	private void lazyInitRowMap()
	{
		load();
	}

	private Date getValidAggregateDate(FlatReportElement aggregate)
	{
		Date date;
		
		try
		{
			date = getAggregateDate(aggregate);
		} catch (ParseException e)
		{
			LOGGER.warn("failed to parse date of " + aggregate, e);
			date = new Date();
		}
		
		return date;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.model.LoadableDetachableModel#onDetach()
	 */
	@Override
	protected void onDetach()
	{
		rowMap = null;
	}
	/**
	 * Get the date of the aggregate. Date format is different per report
	 * @param aggregate
	 * @return
	 */
	protected abstract Date getAggregateDate(FlatReportElement aggregate) throws ParseException;
	
	/**
	 * Get the row key
	 * @param aggregate
	 * @return
	 */
	protected abstract RK getRowKey(FlatReportElement aggregate);
	
	/**
	 * Get row key comparator for sorting
	 * @return
	 */
	protected abstract Comparator<RK> getRKComparator();
}
