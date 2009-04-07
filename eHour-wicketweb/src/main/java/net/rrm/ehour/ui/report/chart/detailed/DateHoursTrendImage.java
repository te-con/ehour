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
