package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.Session;

public class QuickMonthRenderer extends QuickRenderer
{
	private static final long serialVersionUID = 1983255096043016545L;
	private int	currentMonth;
	
	/**
	 * Default constructor
	 */
	public QuickMonthRenderer()
	{
		currentMonth = new GregorianCalendar().get(Calendar.MONTH);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(Object object)
	{
		String	value = null;
		
		if (object instanceof QuickMonth)
		{
			QuickMonth quickMonth = (QuickMonth)object;
			
			int month = quickMonth.getPeriodIndex();
			
			if ( (currentMonth == 0 && month == 11)
					|| (currentMonth - 1 == month))
			{
				value = getLocalizer().getString("report.criteria.previousMonth", null);
			}
			else if (currentMonth == month)
			{
				value = getLocalizer().getString("report.criteria.currentMonth", null);
			}
			else if ( (currentMonth + 1 == month || (currentMonth == 11 && month == 0)))
			{
				value = getLocalizer().getString("report.criteria.nextMonth", null);
			}
			else
			{
				EhourConfig config = ((EhourWebSession)Session.get()).getEhourConfig();
				
				SimpleDateFormat format = new SimpleDateFormat("MMMMM, yyyy", config.getLocale());
				value = format.format(quickMonth.getPeriodStart());
			}
		}
		
		return value;
	}

	/**
	 * 
	 */
	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}

}
