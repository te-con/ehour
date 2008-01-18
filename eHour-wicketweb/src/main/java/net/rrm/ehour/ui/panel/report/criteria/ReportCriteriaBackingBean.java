/**
 * Created on Sep 22, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.panel.report.criteria;

import java.io.Serializable;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickMonth;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickPeriod;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickQuarter;
import net.rrm.ehour.ui.panel.report.criteria.quick.QuickWeek;

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
	
	private int				reportType = 1;
	
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
		this.quickMonth = quickMonth;
		setReportRangeForQuickie(quickMonth);
	}

	public QuickQuarter getQuickQuarter()
	{
		return quickQuarter;
	}

	public void setQuickQuarter(QuickQuarter quickQuarter)
	{
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

	/**
	 * @return the reportType
	 */
	public int getReportType()
	{
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(int reportType)
	{
		this.reportType = reportType;
	}
}
