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

import java.util.Calendar;
import java.util.GregorianCalendar;

public class QuickQuarterRenderer extends QuickRenderer<QuickQuarter>
{
	private static final long serialVersionUID = 9074669170575475399L;
	private int	currentQuarter;
	
	/**
	 * Default constructor
	 */
	public QuickQuarterRenderer()
	{
		currentQuarter = new GregorianCalendar().get(Calendar.MONTH) / 3;
	}
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
	 */
	public Object getDisplayValue(QuickQuarter quickQuarter)
	{
		String	label = null;
		
		int quarter = quickQuarter.getPeriodIndex();
		
		if (currentQuarter == quarter)
		{
			label = getLocalizer().getString("report.criteria.currentQuarter", null);
		}
		else 
		{
			label = getLocalizer().getString("report.criteria.quarter", null) + (quarter + 1);
		}
		
		return label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
	 */
	public String getIdValue(QuickQuarter object, int index)
	{
		return Integer.toString(index);
	}

}
