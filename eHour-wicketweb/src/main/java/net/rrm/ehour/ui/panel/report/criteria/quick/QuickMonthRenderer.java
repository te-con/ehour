package net.rrm.ehour.ui.panel.report.criteria.quick;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.Session;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

public class QuickMonthRenderer implements IChoiceRenderer
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

	/**
	 * Get display value
	 */
	public Object getDisplayValue(Object object)
	{
		String	label = null;
		
		if (object instanceof QuickMonth)
		{
			QuickMonth quickMonth = (QuickMonth)object;
			
			int month = quickMonth.getPeriodIndex();
			
			if ( (currentMonth == 0 && month == 11)
					|| (currentMonth - 1 == month))
			{
				label = "Previous month";
			}
			else if (currentMonth == month)
			{
				label = "Current month";
			}
			else if ( (currentMonth + 1 == month || (currentMonth == 11 && month == 0)))
			{
				label = "Next month";
			}
			else
			{
				EhourConfig config = ((EhourWebSession)Session.get()).getEhourConfig();
				
				SimpleDateFormat format = new SimpleDateFormat("MMMMM", config.getLocale());
				label = format.format(quickMonth.getPeriodStart());
			}
		}
		
		return label;
	}

	/**
	 * 
	 */
	public String getIdValue(Object object, int index)
	{
		return Integer.toString(index);
	}

}
