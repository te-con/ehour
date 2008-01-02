/**
 * Created on Jan 2, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.reportchart.detailed;

import org.apache.wicket.model.Model;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.reportchart.AbstractChartImage;
import net.rrm.ehour.ui.reportchart.rowkey.ChartRowKey;

/**
 * TODO 
 **/

public class DateHoursTrendImage extends AbstractChartImage<FlatReportElement>
{
	private static final long serialVersionUID = -7877973718547907932L;

	public DateHoursTrendImage(String id, Model dataModel, int width, int height)
	{
		super(id, dataModel, width, height);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.AbstractChartImage#getColumnValue(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Number getColumnValue(FlatReportElement element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.AbstractChartImage#getReportNameKey()
	 */
	@Override
	protected String getReportNameKey()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.AbstractChartImage#getRowKey(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected ChartRowKey getRowKey(FlatReportElement element)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.reportchart.AbstractChartImage#getValueAxisLabelKey()
	 */
	@Override
	protected String getValueAxisLabelKey()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
