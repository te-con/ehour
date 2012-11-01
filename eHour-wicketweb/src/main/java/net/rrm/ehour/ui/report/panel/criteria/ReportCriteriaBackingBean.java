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

package net.rrm.ehour.ui.report.panel.criteria;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickPeriod;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.report.panel.criteria.quick.QuickWeek;

import java.io.Serializable;

/**
 * Backing bean for report criteria
 **/

public class ReportCriteriaBackingBean implements Serializable
{
	private static final long serialVersionUID = 4417220135092280759L;

	private ReportCriteria	reportCriteria;
	private QuickWeek		quickWeek;
	private QuickMonth		quickMonth;
	private QuickQuarter	quickQuarter;
	
	public ReportCriteriaBackingBean(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}

	/**
	 * @return the quickWeek
	 */
	public QuickWeek getQuickWeek()
	{
		return quickWeek;
	}

	/**
	 * @param quickWeek the quickWeek to set
	 */
	public void setQuickWeek(QuickWeek quickWeek)
	{
		quickQuarter = null;
		quickMonth = null;
		
		this.quickWeek = quickWeek;
		
		setReportRangeForQuickie(quickWeek);
	}
	
	/**
	 * Set report range based on quickie
	 * @param period
	 */
	private void setReportRangeForQuickie(QuickPeriod period)
	{
		UserCriteria userCriteria = reportCriteria.getUserCriteria(); 
		
		if (userCriteria.getReportRange() == null)
		{
			userCriteria.setReportRange(new DateRange());
		}
		
		if (period != null)
		{
			userCriteria.getReportRange().setDateStart(period.getPeriodStart());
			userCriteria.getReportRange().setDateEnd(period.getPeriodEnd());
		}
	}

	public ReportCriteria getReportCriteria()
	{
		return reportCriteria;
	}

	public QuickMonth getQuickMonth()
	{
		return quickMonth;
	}

	public void setQuickMonth(QuickMonth quickMonth)
	{
		quickWeek = null;
		quickQuarter = null;
		this.quickMonth = quickMonth;
		setReportRangeForQuickie(quickMonth);
	}

	public QuickQuarter getQuickQuarter()
	{
		return quickQuarter;
	}

	public void setQuickQuarter(QuickQuarter quickQuarter)
	{
		quickWeek = null;
		quickMonth = null;
		this.quickQuarter = quickQuarter;
		setReportRangeForQuickie(quickQuarter);
	}

	/**
	 * @param reportCriteria the reportCriteria to set
	 */
	public void setReportCriteria(ReportCriteria reportCriteria)
	{
		this.reportCriteria = reportCriteria;
	}

}
