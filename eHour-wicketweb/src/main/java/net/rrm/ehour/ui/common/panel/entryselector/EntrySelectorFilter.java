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

import org.apache.wicket.model.StringResourceModel;

/**
 * Value object for entry selection
 **/

public class EntrySelectorFilter implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7713534314686523511L;
	private	boolean	activateToggle = true;
	private	String	filterInput;
	private	StringResourceModel	defaultFilterInputText;
	private String	onId;
	
	public EntrySelectorFilter(StringResourceModel defaultFilterInputText)
	{
		this.defaultFilterInputText = defaultFilterInputText;
	}
	
	/**
	 * Get the filter input, remove the default text
	 * @return
	 */
	public String getCleanFilterInput()
	{
		return (filterInput != null 
					&& filterInput.equals(defaultFilterInputText)) ? "" : filterInput;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isActivateToggle()
	{
		return activateToggle;
	}
	
	/**
	 * 
	 * @param activateToggle
	 */
	public void setActivateToggle(boolean activateToggle)
	{
		this.activateToggle = activateToggle;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFilterInput()
	{
		return filterInput == null ? defaultFilterInputText.getString() : filterInput;
	}
	
	/**
	 * 
	 * @param filterInput
	 */
	public void setFilterInput(String filterInput)
	{
		this.filterInput = filterInput;
	}

	/**
	 * @return the onId
	 */
	public String getOnId()
	{
		return onId;
	}

	/**
	 * @param onId the onId to set
	 */
	public void setOnId(String onId)
	{
		this.onId = onId;
	}
}
