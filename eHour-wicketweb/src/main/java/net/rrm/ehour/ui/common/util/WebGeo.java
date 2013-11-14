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

package net.rrm.ehour.ui.common.util;

/**
 * Created on Apr 22, 2009, 12:50:29 AM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public enum WebGeo {
    NOT_DEFINED(-1),
    W_CONTENT_XXSMALL(300),
    W_CONTENT_XSMALL(350),
    W_CONTENT_SMALL(450),
    W_CONTENT_MEDIUM(730),
    W_CONTENT_WIDE(950),
    W_CONTENT_ADMIN_TAB(500),
    W_CONTENT_ADMIN_TAB_WIDE(550),
    W_ENTRY_SELECTOR(250),
    W_CHART_SMALL(350),
    W_CHART_MEDIUM(460),
    W_CHART_WIDE(700),
    H_CHART(200),
    W_FULL(95, "%"),
    AUTO(0);

    private Integer value;
    private String unit;

    private WebGeo(Integer value) {
        this(value, "px");
    }


    private WebGeo(Integer value, String unit) {
        this.value = value;
        this.unit = unit;
    }


    @Override
    public String toString() {
        return String.format("%d%s", value, unit);
    }
}
