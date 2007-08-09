/**
 * Created on Aug 9, 2007
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

package net.rrm.ehour.ui.panel.entryselector;

import java.io.Serializable;

/**
 * Value object for entry selection
 **/

public class EntrySelectorFilter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7713534314686523511L;
	private	boolean	activateToggle;
	private	String	filterInput;
	private	String	defaultFilterInputText;
	
	public EntrySelectorFilter()
	{
		
	}
	
	public EntrySelectorFilter(String defaultFilterInputText)
	{
		this.filterInput = defaultFilterInputText;
		this.defaultFilterInputText = defaultFilterInputText;
	}
	
	/**
	 * Get the filter input, remove the default text
	 * @return
	 */
	public String getCleanFilterInput()
	{
		return filterInput.equals(defaultFilterInputText) ? "" : filterInput;
	}
	
	public boolean isActivateToggle()
	{
		return activateToggle;
	}
	public void setActivateToggle(boolean activateToggle)
	{
		this.activateToggle = activateToggle;
	}
	public String getFilterInput()
	{
		return filterInput;
	}
	
	public void setFilterInput(String filterInput)
	{
		this.filterInput = filterInput;
	}
}
