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

public class QuickQuarter extends QuickPeriod {
    private static final long serialVersionUID = -2058684279683057511L;

    private QuickQuarter() {
        super();
    }

    private QuickQuarter(Date periodStart, Date periodEnd, int periodIndex, QuickType shortcut) {
        super(periodStart, periodEnd, periodIndex, shortcut);
    }

    public static QuickQuarter divider() {
        return new QuickQuarter();
    }

    public static QuickQuarter shortcut(Calendar calendarOrig, QuickType quickType) {
        return instance(calendarOrig, quickType);
    }

    public static QuickQuarter instance(Calendar calendarOrig) {
        return instance(calendarOrig, QuickType.NONE);
    }

    private static QuickQuarter instance(Calendar calendarOrig, QuickType quickType) {
        Calendar cal = (Calendar) calendarOrig.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date periodStart = cal.getTime();

        int periodIndex = cal.get(Calendar.MONTH) / 3;

        cal.add(Calendar.MONTH, 3);
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date periodEnd = cal.getTime();

        return new QuickQuarter(periodStart, periodEnd, periodIndex, quickType);
    }
}