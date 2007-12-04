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
