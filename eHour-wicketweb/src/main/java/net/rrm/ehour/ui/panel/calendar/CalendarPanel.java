/**
 * Created on May 8, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.panel.calendar;

import net.rrm.ehour.ui.panel.nav.MainNavPanel;
import net.rrm.ehour.ui.panel.sidepanel.SidePanel;
import wicket.ResourceReference;
import wicket.markup.html.resources.CompressedResourceReference;
import wicket.markup.html.resources.StyleSheetReference;

/**
 * Navigation Calendar 
 **/

public class CalendarPanel extends SidePanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7777893083323915299L;


	/**
	 * 
	 * @param id
	 */
	public CalendarPanel(String id)
	{
		super(id);
		
		add(new StyleSheetReference("calendarStyle", calendarStyle()));
	}
	
	
	/**
	 * Create a style
	 * 
	 * @return a style
	 */
	public final ResourceReference calendarStyle()
	{
		return new CompressedResourceReference(MainNavPanel.class, "style/calendar.css");
	}	
}
