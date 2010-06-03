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

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.service.report.reports.ReportData;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.report.Report;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.junit.Before;

/**
 * Created on Mar 12, 2009, 10:36:37 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractAggregateChartImageTest extends AbstractSpringWebAppTester
{
	protected Report report;
	protected ReportData reportData;
	
	@Before
	public void before()
	{
		reportData = new ReportData(ReportTestUtil.getAssignmentAggregateReportElements(), new DateRange());
		
		report = new Report()
		{
			private static final long serialVersionUID = -4485933249826394111L;

			public ReportCriteria getReportCriteria()
			{
				return null;
			}

			public ReportData getReportData()
			{
				return reportData;
			}

			public DateRange getReportRange()
			{
				return null;
			}
		
		};		
	}
}
