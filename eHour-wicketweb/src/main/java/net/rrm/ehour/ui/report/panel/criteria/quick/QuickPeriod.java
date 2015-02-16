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

import java.io.Serializable;
import java.util.Date;


public class QuickPeriod implements Serializable {
    private static final long serialVersionUID = -245086949586026553L;

    private Date periodStart;
    private Date periodEnd;

    private int periodIndex;

    private QuickType quickType;

    public QuickPeriod() {
        this.quickType = QuickType.DIVIDER;
    }

    public QuickPeriod(Date periodStart, Date periodEnd, int periodIndex, QuickType quickType) {
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.periodIndex = periodIndex;
        this.quickType = quickType;
    }

    public boolean isDivider() {
        return quickType == QuickType.DIVIDER;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public int getPeriodIndex() {
        return periodIndex;
    }

    public QuickType getQuickType() {
        return quickType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuickPeriod that = (QuickPeriod) o;

        return periodIndex == that.periodIndex;
    }

    @Override
    public int hashCode() {
        return periodIndex;
    }

    public enum QuickType {
        SHORTCUT_PREV,
        SHORTCUT_CURRENT,
        SHORTCUT_NEXT,
        NONE,
        DIVIDER
    }
}
