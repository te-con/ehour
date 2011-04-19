package net.rrm.ehour.ui.timesheet.converter;

import net.rrm.ehour.ui.common.converter.FloatConverter;

public class TimesheetFloatConverter extends FloatConverter
{
	private static final long serialVersionUID = 591630601527749134L;

	private static final TimesheetFloatConverter instance = new TimesheetFloatConverter();
	
	private TimesheetFloatConverter()
	{
		super("");
	}
	
	public static TimesheetFloatConverter getInstance()
	{
		return instance;
	}
}
