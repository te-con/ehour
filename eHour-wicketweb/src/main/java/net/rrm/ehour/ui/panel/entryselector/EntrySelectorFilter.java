/**
 * Created on Aug 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
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
	private	boolean	activateToggle = true;
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
		return (filterInput != null && filterInput.equals(defaultFilterInputText)) ? "" : filterInput;
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
