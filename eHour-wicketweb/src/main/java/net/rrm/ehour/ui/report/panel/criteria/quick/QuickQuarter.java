package net.rrm.ehour.ui.report.panel.criteria.quick;

import java.util.Calendar;

public class QuickQuarter extends QuickPeriod
{
	private static final long serialVersionUID = -2058684279683057511L;

	/**
	 * 
	 * @param calendarOrig
	 */
	public QuickQuarter(Calendar calendarOrig)
	{
		Calendar cal = (Calendar)calendarOrig.clone();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		setPeriodStart(cal.getTime());
		
		setPeriodIndex(cal.get(Calendar.MONTH) / 3);
		
		cal.add(Calendar.MONTH, 3);
		cal.add(Calendar.DAY_OF_YEAR, -1);
		setPeriodEnd(cal.getTime());
	}
}