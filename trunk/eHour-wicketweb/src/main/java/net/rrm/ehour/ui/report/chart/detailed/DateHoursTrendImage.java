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

package net.rrm.ehour.ui.report.chart.detailed;

import java.util.Locale;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import net.rrm.ehour.ui.report.chart.rowkey.DateRowKey;

import org.apache.wicket.model.IModel;

/**
 * Hours per date chart
 **/

public class DateHoursTrendImage extends AbstractTrendChartImage<FlatReportElement>
{
	private static final long serialVersionUID = -7877973718547907932L;
	private Locale locale;
	
	public DateHoursTrendImage(String id, IModel dataModel, int width, int height, String seriesColumn)
	{
		super(id, dataModel, width, height, seriesColumn);
		
		locale = EhourWebSession.getSession().getEhourConfig().getLocale();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.AbstractReportChartImage#getColumnValue(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Number getColumnValue(FlatReportElement element)
	{
		return element.getTotalHours();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.AbstractReportChartImage#getReportNameKey()
	 */
	@Override
	protected String getReportNameKey()
	{
		return "report.hoursDay";
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.AbstractReportChartImage#getRowKey(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected ChartRowKey getRowKey(FlatReportElement element)
	{
		return new DateRowKey(element.getDayDate(), locale);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.AbstractReportChartImage#getValueAxisLabelKey()
	 */
	@Override
	protected String getValueAxisLabelKey()
	{
		return "general.hours";
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.detailed.AbstractTrendChartImage#getSeriesKey(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Object getSeriesKey(FlatReportElement element)
	{
		if (seriesColumnIndex.equals("userReport.report.user") )
		{
			return element.getUserId();	
		}
		else if (seriesColumnIndex.equals("userReport.report.customer") )
		{
			return element.getCustomerId();
		}
		else
		{
			return element.getProjectId();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.chart.detailed.AbstractTrendChartImage#getSeriesName(net.rrm.ehour.report.reports.element.ReportElement)
	 */
	@Override
	protected Comparable<?> getSeriesName(FlatReportElement element)
	{
		if (seriesColumnIndex.equals("userReport.report.user") )
		{
			return element.getUserLastName();	
		}
		else if (seriesColumnIndex.equals("userReport.report.customer") )
		{
			return element.getCustomerName();
		}
		else
		{
			return element.getProjectName();
		}
	}
}
