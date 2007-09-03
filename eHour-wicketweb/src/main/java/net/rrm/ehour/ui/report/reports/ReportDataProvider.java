/**
 * Created on Jul 12, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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
