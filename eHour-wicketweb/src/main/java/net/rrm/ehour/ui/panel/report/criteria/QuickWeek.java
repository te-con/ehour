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
import java.util.Calendar;
import java.util.Date;

/**
 * TODO 
 **/

public class QuickWeek implements Serializable
{
	private static final long serialVersionUID = -8803620859213666342L;

	private Date	weekStart;
	private Date	weekEnd;
	
	public QuickWeek(Calendar calendarOrig)
	{
		Calendar cal = (Calendar)calendarOrig.clone();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		weekStart = cal.getTime();
		
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		weekEnd = cal.getTime();
	}

	/**
	 * @return the weekStart
	 */
	public Date getWeekStart()
	{
		return weekStart;
	}

	/**
	 * @param weekStart the weekStart to set
	 */
	public void setWeekStart(Date weekStart)
	{
		this.weekStart = weekStart;
	}

	/**
	 * @return the weekEnd
	 */
	public Date getWeekEnd()
	{
		return weekEnd;
	}

	/**
	 * @param weekEnd the weekEnd to set
	 */
	public void setWeekEnd(Date weekEnd)
	{
		this.weekEnd = weekEnd;
	}
}