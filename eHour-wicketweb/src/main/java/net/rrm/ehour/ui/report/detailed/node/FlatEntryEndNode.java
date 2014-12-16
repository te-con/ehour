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

package net.rrm.ehour.ui.report.detailed.node;

import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.report.reports.element.ReportElement;
import net.rrm.ehour.ui.report.model.ReportNode;

import java.io.Serializable;

/**
 * Entry end node
 */

public class FlatEntryEndNode extends ReportNode {
    private static final long serialVersionUID = 7854152602780377915L;
    private Number hours;
    private Number turnOver;

    public FlatEntryEndNode(FlatReportElement element) {
        super(element.getDisplayOrder(), element.isEmptyEntry());
        hours = element.getTotalHours();
        turnOver = element.getTotalTurnOver();

        this.columnValues = new Serializable[]{element.getComment(), element.getRate(), hours, turnOver};
    }

    @Override
    protected Serializable getElementId(ReportElement element) {
        return ((FlatReportElement) element).getDisplayOrder();
    }

    @Override
    public Number getHours() {
        return hours;
    }

    @Override
    public Number getTurnover() {
        return turnOver;
    }

    @Override
    protected boolean isLeaf() {
        return true;
    }
}
