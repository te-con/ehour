/**
 * Created on Oct 5, 2007
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.report.trend;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.ui.report.Report;
import net.rrm.ehour.util.DateUtil;

/**
 * Base trend report
 **/

@SuppressWarnings("unchecked")
public abstract class TrendReport<RK extends Comparable> extends Report
{
	protected SortedMap<RK, Map<Date, FlatProjectAssignmentAggregate>>	rowMap;

	/**
	 * Initialize the report
	 * @param aggregateData
	 * @throws ParseException 
	 */
	public void initialize(List<FlatProjectAssignmentAggregate> aggregateData) throws ParseException
	{
		Map<Date, FlatProjectAssignmentAggregate>	rowAggregates;
		Date	aggregateDate;
		RK		rowKey;

		if (aggregateData == null)
		{
			return;
		}
		
		rowMap = new TreeMap<RK, Map<Date, FlatProjectAssignmentAggregate>>(getRKComparator());

		for (FlatProjectAssignmentAggregate aggregate : aggregateData)
		{
			rowKey = getRowKey(aggregate);
			
			if (rowMap.containsKey(rowKey))
			{
				rowAggregates = rowMap.get(rowKey);
			}
			else
			{
				rowAggregates = new HashMap<Date, FlatProjectAssignmentAggregate>();
			}
			
			aggregateDate = getAggregateDate(aggregate);
			aggregateDate = DateUtil.nullifyTime(aggregateDate);
			
			rowAggregates.put(aggregateDate, aggregate);
			
			rowMap.put(rowKey, rowAggregates);
		}
	}

	/**
	 * Get grand total hours
	 * @return
	 */
	public float getGrandTotalHours()
	{
		float total = 0;
		Map<Date, FlatProjectAssignmentAggregate>	aggMap;
		
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
	public SortedMap<RK, Map<Date, FlatProjectAssignmentAggregate>> getValues()
	{
		return rowMap;
	}

	/**
	 * Get the date of the aggregate. Date format is different per report
	 * @param aggregate
	 * @return
	 */
	protected abstract Date getAggregateDate(FlatProjectAssignmentAggregate aggregate) throws ParseException;
	
	/**
	 * Get the row key
	 * @param aggregate
	 * @return
	 */
	protected abstract RK getRowKey(FlatProjectAssignmentAggregate aggregate);
	
	/**
	 * Get row key comparator for sorting
	 * @return
	 */
	protected abstract Comparator<RK> getRKComparator();
}
