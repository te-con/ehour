/**
 * Created on Mar 3, 2007
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

package net.rrm.ehour.web.report.reports.trend;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import net.rrm.ehour.report.reports.FlatProjectAssignmentAggregate;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Generic timeline report 
 * RK = Row Key
 **/

public abstract class TimelineReport<RK extends Comparable>
{
	protected Logger			logger = Logger.getLogger(this.getClass());
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
