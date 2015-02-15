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

import org.apache.wicket.Localizer;

/**
 * @author Thies
 */
public class QuickWeekRenderer extends QuickRenderer<QuickWeek> {
    private static final long serialVersionUID = -7860131083740371031L;

    @Override
    protected Object getValue(QuickWeek quickWeek) {
        Localizer localizer = getLocalizer();

        switch (quickWeek.getQuickType()) {
            case NONE:
            default:
                String value = localizer.getString("report.criteria.week", null);
                value += " " + quickWeek.getPeriodIndex();
                return value;
            case SHORTCUT_PREV:
                return localizer.getString("report.criteria.previousWeek", null);
            case SHORTCUT_CURRENT:
                return localizer.getString("report.criteria.currentWeek", null);
            case SHORTCUT_NEXT:
                return localizer.getString("report.criteria.nextWeek", null);

        }
    }

    public String getIdValue(QuickWeek object, int index) {
        return Integer.toString(index);
    }
}
