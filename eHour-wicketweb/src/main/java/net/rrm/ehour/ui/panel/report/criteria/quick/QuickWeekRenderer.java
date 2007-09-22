/**
 * 
 */
package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author Thies
 *
 */
public class QuickWeekRenderer implements IChoiceRenderer
{
	private static final long serialVersionUID = -7860131083740371031L;
	private Date	currentWeekStart;
	private Date	previousWeekStart;
	private Date	nextWeekEnd;
	private Date	nextWeekStart;
	
	/**
	 * Default constructor
	 */
	public QuickWeekRenderer()
	{
		Calendar currentWeekStartCal = new GregorianCalendar();
		currentWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		DateUtil.nullifyTime(currentWeekStartCal);
		currentWeekStart = currentWeekStartCal.getTime();
		
		Calendar previousWeekStartCal = new GregorianCalendar();
		previousWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		previousWeekStartCal.add(Calendar.WEEK_OF_YEAR, -1);
		DateUtil.nullifyTime(previousWeekStartCal);
		previousWeekStart = previousWeekStartCal.getTime();
		
		Calendar nextWeekEndCal = new GregorianCalendar();
		nextWeekEndCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		nextWeekEndCal.add(Calendar.WEEK_OF_YEAR, 2);
		DateUtil.nullifyTime(nextWeekEndCal);
		nextWeekEnd = nextWeekEndCal.getTime();

		Calendar nextWeekStartCal = new GregorianCalendar();
		nextWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		nextWeekStartCal.add(Calendar.WEEK_OF_YEAR, 1);
		DateUtil.nullifyTime(nextWeekEndCal);
		nextWeekStart = nextWeekStartCal.getTime();

	}
	
	/**
	 * TODO i18n 
	 */
	public Object getDisplayValue(Object object)
	{
		if (object instanceof QuickWeek)
		{
			QuickWeek	quickWeek = (QuickWeek)object;
		
			if (quickWeek.getPeriodStart().before(previousWeekStart)
					|| quickWeek.getPeriodStart().after(nextWeekEnd))
			{
				return "Week " + quickWeek.getPeriodIndex();
			}
			else if (quickWeek.getPeriodStart().before(currentWeekStart))
			{
				return "Previous week";
			}
			else if (quickWeek.getPeriodStart().before(nextWeekStart))
			{
				return "Current week";
			}
			else
			{
				return "Next week";
			}
		}
		return "unknown";
	}

	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}

}
