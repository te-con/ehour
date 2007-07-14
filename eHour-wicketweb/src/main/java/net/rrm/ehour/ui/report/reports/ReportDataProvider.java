/**
 * Created on Jul 12, 2007
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

package net.rrm.ehour.ui.report.reports;

import java.io.Serializable;
import java.util.Iterator;

import net.rrm.ehour.ui.report.reports.aggregate.AggregateReport;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Report data provider
 **/

public class ReportDataProvider extends SortableDataProvider
{
	private static final long serialVersionUID = -1898730172009882583L;

	private AggregateReport<?, ?, ?>	reportData;
	
	/**
	 * 
	 * @param reportData
	 */
	public ReportDataProvider(AggregateReport<?, ?, ?> reportData)
	{
		this.reportData = reportData;
	}
	
	/**
	 * Get an iterator on a subset 
	 */
	@SuppressWarnings("unchecked")
	public Iterator iterator(int first, int count)
	{
		return reportData.getReportNodes().subList(first, first + count).iterator();
	}

	/**
	 * 
	 */
	public IModel model(Object object)
	{
		return new Model((Serializable)object);
	}

	/**
	 * 
	 */
	public int size()
	{
		return reportData.getReportNodes().size();
	}

	/**
	 * 
	 */
	public void detach()
	{
	}
}
