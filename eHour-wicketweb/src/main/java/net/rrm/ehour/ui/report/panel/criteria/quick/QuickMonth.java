package net.rrm.ehour.ui.report.panel.criteria.quick;

import java.util.Calendar;

public class QuickMonth extends QuickPeriod
{
	private static final long serialVersionUID = 2826862640605212133L;

	/**
	 * 
	 * @param calendarOrig
	 */
	public QuickMonth(Calendar calendarOrig)
	{
		Calendar cal = (Calendar)calendarOrig.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		setPeriodStart(cal.getTime());
		
		setPeriodIndex(cal.get(Calendar.MONTH));
		
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		setPeriodEnd(cal.getTime());
	}
}
