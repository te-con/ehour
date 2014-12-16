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

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.report.model;

import net.rrm.ehour.report.reports.element.ReportElement;

import java.io.Serializable;

/**
 * Created on Mar 12, 2009, 1:55:18 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class TreeReportElement implements ReportElement {
    private static final long serialVersionUID = 7162514980734420038L;

    private Serializable[] row;

    private boolean empty;

    public TreeReportElement(Serializable[] row) {
        this(row, false);
    }

    public TreeReportElement(Serializable[] row, boolean empty) {
        this.row = row;
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Serializable[] getRow() {
        return row;
    }
}
