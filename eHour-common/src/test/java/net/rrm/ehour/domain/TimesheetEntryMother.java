package net.rrm.ehour.domain;

import java.util.Date;

/**
 * Created on Feb 7, 2010 2:47:20 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class TimesheetEntryMother
{
	public static TimesheetEntry getTimesheetEntry(int prjId, Date date, float hours)
	{
		TimesheetEntry 	entry;
		TimesheetEntryId id;
		
		id = new TimesheetEntryId();
		id.setEntryDate(date);
		id.setActivity(ActivityMother.createActivity(prjId, prjId, 1));
		
		entry = new TimesheetEntry();
		entry.setEntryId(id);
		entry.setHours(hours);
		
		return entry;
	}
}
