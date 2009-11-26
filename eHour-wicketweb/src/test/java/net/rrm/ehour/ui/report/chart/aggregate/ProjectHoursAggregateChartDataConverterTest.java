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

import org.apache.wicket.model.Model;
import org.junit.Test;

public class ProjectHoursAggregateChartDataConverterTest extends AbstractAggregateChartImageTest
{
	@Test
	public void testChartImage() throws Exception
	{
		ProjectHoursAggregateChartDataConverter provider = new ProjectHoursAggregateChartDataConverter();
		
		AggregateChartImage img = new AggregateChartImage("image", new Model<ReportData>(reportData), 200, 100, provider);
		img.getChart(reportData);		
	}
}
