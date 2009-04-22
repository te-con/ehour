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
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
public enum WebWidth
{
	DEFAULT(-1),
	CONTENT_XSMALL(350),
	CONTENT_SMALL(450),
	CONTENT_MEDIUM(730),
	CONTENT_WIDE(950),
	CONTENT_ADMIN_TAB(500),
	ENTRYSELECTOR_WIDTH(250),
	CHART_SMALL(350),
	CHART_MEDIUM(460),
	CHART_WIDE(700),
	CHART_HEIGHT(200);
	
	private Integer width;
	
	private WebWidth(Integer width)
	{
		this.width = width;
	}
	
	public Integer getWidth()
	{
		return width;
	}
	
	
}
