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

package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.util.Calendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.util.DateUtil;

/**
 * Quick week value object
 **/

public class QuickWeek extends QuickPeriod
{
	private static final long serialVersionUID = -8803620859213666342L;

	public QuickWeek(Calendar calendarOrig, EhourConfig config)
	{
		Calendar cal = (Calendar)calendarOrig.clone();
		DateUtil.dayOfWeekFix(cal);
		cal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
		setPeriodStart(cal.getTime());
		
		setPeriodIndex(cal.get(Calendar.WEEK_OF_YEAR));
		
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		setPeriodEnd(cal.getTime());
	}
}