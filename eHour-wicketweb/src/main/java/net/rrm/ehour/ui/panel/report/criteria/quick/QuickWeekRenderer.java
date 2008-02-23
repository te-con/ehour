/**
 * 
 */
package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.rrm.ehour.util.DateUtil;

import org.apache.wicket.Localizer;
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
	private Localizer localizer;
	
	/**
	 * Default constructor
	 */
	public QuickWeekRenderer(Localizer localizer)
	{
		Calendar currentWeekStartCal = new GregorianCalendar();
		currentWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		currentWeekStartCal.setFirstDayOfWeek(Calendar.SUNDAY);
		DateUtil.nullifyTime(currentWeekStartCal);
		currentWeekStart = currentWeekStartCal.getTime();
		
		Calendar previousWeekStartCal = new GregorianCalendar();
		previousWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		previousWeekStartCal.setFirstDayOfWeek(Calendar.SUNDAY);
		previousWeekStartCal.add(Calendar.WEEK_OF_YEAR, -1);
		DateUtil.nullifyTime(previousWeekStartCal);
		previousWeekStart = previousWeekStartCal.getTime();
		
		Calendar nextWeekEndCal = new GregorianCalendar();
		nextWeekEndCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		nextWeekEndCal.setFirstDayOfWeek(Calendar.SUNDAY);
		nextWeekEndCal.add(Calendar.WEEK_OF_YEAR, 2);
		DateUtil.nullifyTime(nextWeekEndCal);
		nextWeekEnd = nextWeekEndCal.getTime();

		Calendar nextWeekStartCal = new GregorianCalendar();
		nextWeekStartCal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		nextWeekStartCal.setFirstDayOfWeek(Calendar.SUNDAY);
		nextWeekStartCal.add(Calendar.WEEK_OF_YEAR, 1);
		DateUtil.nullifyTime(nextWeekEndCal);
		nextWeekStart = nextWeekStartCal.getTime();

		this.localizer = localizer;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object object)
	{
		String value = "unknown";
		
		if (object instanceof QuickWeek)
		{
			QuickWeek	quickWeek = (QuickWeek)object;
		
			if (quickWeek.getPeriodStart().before(previousWeekStart)
					|| quickWeek.getPeriodStart().after(nextWeekEnd))
			{
				value = localizer.getString("report.criteria.week", null);
				value += " " + quickWeek.getPeriodIndex();
			}
			else if (quickWeek.getPeriodStart().before(currentWeekStart))
			{
				value = localizer.getString("report.criteria.previousWeek", null);
			}
			else if (quickWeek.getPeriodStart().before(nextWeekStart))
			{
				value = localizer.getString("report.criteria.currentWeek", null);
			}
			else
			{
				value = localizer.getString("report.criteria.nextWeek", null);
			}
		}
		
		return value;
	}

	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}

}
