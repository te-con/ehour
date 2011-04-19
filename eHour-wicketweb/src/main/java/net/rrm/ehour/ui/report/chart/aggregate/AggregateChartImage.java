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

package net.rrm.ehour.ui.report.chart.aggregate;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.ui.report.chart.AbstractReportChartImage;
import net.rrm.ehour.ui.report.chart.AggregateChartDataConverter;
import net.rrm.ehour.ui.report.chart.rowkey.ChartRowKey;
import org.apache.wicket.model.IModel;

/**
 * Created on Mar 17, 2009, 6:33:20 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public class AggregateChartImage extends AbstractReportChartImage<AssignmentAggregateReportElement>
{
	private static final long serialVersionUID = 7857936136458323251L;
	private AggregateChartDataConverter provider;
	
	public AggregateChartImage(String id, IModel<ReportData> dataModel, int width, int height, AggregateChartDataConverter provider)
	{
		super(id, dataModel, width, height);
		
		this.provider = provider;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.chart.AbstractReportChartImage#getColumnValue(net.rrm.ehour.persistence.persistence.report.reports.importer.ReportElement)
	 */
	@Override
	protected Number getColumnValue(AssignmentAggregateReportElement element)
	{
		return provider.getColumnValue(element);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.chart.AbstractReportChartImage#getReportNameKey()
	 */
	@Override
	protected String getReportNameKey()
	{
		return provider.getReportNameKey();
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.chart.AbstractReportChartImage#getRowKey(net.rrm.ehour.persistence.persistence.report.reports.importer.ReportElement)
	 */
	@Override
	protected ChartRowKey getRowKey(AssignmentAggregateReportElement element)
	{
		return provider.getRowKey(element);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.report.chart.AbstractReportChartImage#getValueAxisLabelKey()
	 */
	@Override
	protected String getValueAxisLabelKey()
	{
		return provider.getValueAxisLabelKey();
	}
}
