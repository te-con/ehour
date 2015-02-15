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
import net.rrm.ehour.util.DateUtil;
import org.apache.wicket.Localizer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Thies
 */
public class QuickWeekRenderer extends QuickRenderer<QuickWeek> {
    private static final long serialVersionUID = -7860131083740371031L;
    private Date currentWeekStart;
    private Date previousWeekStart;
    private Date nextWeekEnd;
    private Date nextWeekStart;

    public QuickWeekRenderer(EhourConfig config) {
        Calendar currentWeekStartCal = new GregorianCalendar();
        DateUtil.dayOfWeekFix(currentWeekStartCal);
        currentWeekStartCal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        currentWeekStartCal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        DateUtil.nullifyTime(currentWeekStartCal);
        currentWeekStart = currentWeekStartCal.getTime();

        Calendar previousWeekStartCal = new GregorianCalendar();
        DateUtil.dayOfWeekFix(previousWeekStartCal);
        previousWeekStartCal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        previousWeekStartCal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        previousWeekStartCal.add(Calendar.WEEK_OF_YEAR, -1);
        DateUtil.nullifyTime(previousWeekStartCal);
        previousWeekStart = previousWeekStartCal.getTime();

        Calendar nextWeekEndCal = new GregorianCalendar();
        DateUtil.dayOfWeekFix(nextWeekEndCal);
        nextWeekEndCal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        nextWeekEndCal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        nextWeekEndCal.add(Calendar.WEEK_OF_YEAR, 2);
        DateUtil.nullifyTime(nextWeekEndCal);
        nextWeekEnd = nextWeekEndCal.getTime();

        Calendar nextWeekStartCal = new GregorianCalendar();
        DateUtil.dayOfWeekFix(nextWeekStartCal);
        nextWeekStartCal.setFirstDayOfWeek(config.getFirstDayOfWeek());
        nextWeekStartCal.set(Calendar.DAY_OF_WEEK, config.getFirstDayOfWeek());
        nextWeekStartCal.add(Calendar.WEEK_OF_YEAR, 1);
        DateUtil.nullifyTime(nextWeekStartCal);
        nextWeekStart = nextWeekStartCal.getTime();
    }

    /*
     * (non-Javadoc)
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getDisplayValue(java.lang.Object)
     */
    public Object getDisplayValue(QuickWeek quickWeek) {
        Localizer localizer = getLocalizer();

        String value;

        if (quickWeek.getPeriodStart().before(previousWeekStart)
                || quickWeek.getPeriodStart().after(nextWeekEnd)) {
            value = localizer.getString("report.criteria.week", null);
            value += " " + quickWeek.getPeriodIndex();
        } else if (quickWeek.getPeriodStart().before(currentWeekStart)) {
            value = localizer.getString("report.criteria.previousWeek", null);
        } else if (quickWeek.getPeriodStart().before(nextWeekStart)) {
            value = localizer.getString("report.criteria.currentWeek", null);
        } else {
            value = localizer.getString("report.criteria.nextWeek", null);
        }

        return value;
    }

    /*
     * (non-Javadoc)
     * @see org.apache.wicket.markup.html.form.IChoiceRenderer#getIdValue(java.lang.Object, int)
     */
    public String getIdValue(QuickWeek object, int index) {
        return Integer.toString(index);
    }
}
