/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.report.panel.criteria.quick;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class QuickMonthRenderer extends QuickRenderer<QuickMonth>
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
	public Object getDisplayValue(QuickMonth quickMonth)
	{
		String	value;
		
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
			EhourConfig config = EhourWebSession.getEhourConfig();
			
			SimpleDateFormat format = new SimpleDateFormat("MMMMM, yyyy", config.getFormattingLocale());
			value = format.format(quickMonth.getPeriodStart());
		}
		
		return value;
	}

	/**
	 * 
	 */
	public String getIdValue(QuickMonth object, int index)
	{
		return Integer.toString(index);
	}

}
