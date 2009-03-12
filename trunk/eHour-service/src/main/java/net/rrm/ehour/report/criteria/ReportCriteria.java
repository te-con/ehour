/**
 * Created on Jan 14, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.criteria;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.util.DateUtil;

import org.apache.log4j.Logger;

/**
 * Container for the report criteria, available and user selected 
 **/

public class ReportCriteria implements Serializable
{
	private static final long serialVersionUID = 7406265452950554098L;
	private	final static Logger	logger = Logger.getLogger(ReportCriteria.class);
	private	AvailableCriteria	availableCriteria;
	private	UserCriteria		userCriteria;
	
	/**
	 * Default constructor
	 *
	 */
	public ReportCriteria()
	{
		this(new AvailableCriteria(), new UserCriteria());
	}

	public ReportCriteria(AvailableCriteria availableCriteria)
	{
		this(availableCriteria, new UserCriteria());
	}
	
	public ReportCriteria(UserCriteria userCriteria)
	{
		this(new AvailableCriteria(), userCriteria);
	}	

	public ReportCriteria(AvailableCriteria availableCriteria, UserCriteria userCriteria)
	{
		this.availableCriteria = availableCriteria;
		this.userCriteria = userCriteria;
	}

	/**
	 * Get report range, use the available criteria if the user didn't supply any (yet)
	 * @return
	 */
	// TODO reduce complexity of method 
	public DateRange getReportRange()
	{
		DateRange	reportRange;
		
		if (userCriteria.getReportRange() == null)
		{
			reportRange = availableCriteria.getReportRange();
		}
		else
		{
			reportRange = userCriteria.getReportRange();
			
			if (reportRange.getDateStart() == null || userCriteria.isInfiniteStartDate())
			{
				if (availableCriteria == null || availableCriteria.getReportRange() == null)
				{
					reportRange.setDateStart(new Date());
				}
				else
				{
					reportRange.setDateStart(availableCriteria.getReportRange().getDateStart());
				}
			}
			
			if (reportRange.getDateEnd() == null || userCriteria.isInfiniteEndDate())
			{
				if (availableCriteria == null || availableCriteria.getReportRange() == null)
				{
					reportRange.setDateEnd(new Date());
				}
				else
				{
					reportRange.setDateEnd(availableCriteria.getReportRange().getDateEnd());
				}
			}
		}

		userCriteria.setReportRange(reportRange);

		// if no timesheets were specified, use the current month as the range
		if (reportRange == null || reportRange.isEmpty())
		{
			reportRange = DateUtil.calendarToMonthRange(new GregorianCalendar());
		}
		
		logger.debug("Using report range " + reportRange);
		
		return reportRange;
	}

	/**
	 * @return the userCriteria
	 */
	public UserCriteria getUserCriteria()
	{
		return userCriteria;
	}

	/**
	 * @return the availableCriteria
	 */
	public AvailableCriteria getAvailableCriteria()
	{
		return availableCriteria;
	}
}