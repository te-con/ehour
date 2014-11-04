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

package net.rrm.ehour.ui.common.panel.entryselector;

import java.io.Serializable;

/**
 * Value object for entry selection
 * (not quite clear why this Boolean wrapper exists..)
 */

public class HideInactiveFilter implements Serializable {
    private static final long serialVersionUID = -7713534314686523511L;
    private boolean hideInactive = true;

	public HideInactiveFilter() {

	}

	public HideInactiveFilter(boolean hideInactive) {
		this.hideInactive = hideInactive;
	}

	public boolean isHideInactive() {
        return hideInactive;
    }

    public void setHideInactive(boolean hideInactive) {
        this.hideInactive = hideInactive;
    }
}
