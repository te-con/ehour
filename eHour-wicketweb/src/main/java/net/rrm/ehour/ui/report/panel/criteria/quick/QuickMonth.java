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
import java.util.Date;

public class QuickMonth extends QuickPeriod {
    private static final long serialVersionUID = 2826862640605212133L;

    private QuickMonth(Date periodStart, Date periodEnd, int periodIndex, boolean shortcut) {
        super(periodStart, periodEnd, periodIndex, shortcut);
    }

    public static QuickMonth instance(Calendar calendarOrig) {
        Calendar cal = (Calendar) calendarOrig.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date periodStart = cal.getTime();

        int periodIndex = cal.get(Calendar.MONTH);

        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date periodEnd = cal.getTime();
        return new QuickMonth(periodStart, periodEnd, periodIndex, true);
    }
}
