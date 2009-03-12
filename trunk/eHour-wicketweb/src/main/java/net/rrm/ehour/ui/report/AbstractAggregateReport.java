/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.report;

import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.ui.common.report.ReportConfig;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Mar 12, 2009, 10:21:18 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public abstract class AbstractAggregateReport extends TreeReport
{
	private static final long serialVersionUID = -7459442244875136235L;
	@SpringBean
	private AggregateReportService aggregateReportService;
	
	public AbstractAggregateReport(ReportCriteria reportCriteria, ReportConfig reportConfig)
	{
		super(reportCriteria, reportConfig);
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.ui.report.TreeReport#fetchReportData(net.rrm.ehour.report.criteria.ReportCriteria)
	 */
	@Override
	protected final ReportData fetchReportData(ReportCriteria reportCriteria)
	{
		return getAggregateReportService().getAggregateReportData(reportCriteria);
	}

	private AggregateReportService getAggregateReportService()
	{
		if (aggregateReportService == null)
		{
			CommonWebUtil.springInjection(this);
		}
		
		return aggregateReportService;
	}
}
