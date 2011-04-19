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