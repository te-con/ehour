/**
 * Created on Dec 9, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Transfer object for ajax events
 **/

public class AjaxEvent
{
	private AjaxRequestTarget target;
	
	private AjaxEventType	eventType;
	
	/**
	 * 
	 */
	public AjaxEvent()
	{
	}
	
	/**
	 * 
	 * @param target
	 * @param eventType
	 */
	public AjaxEvent(AjaxRequestTarget target, AjaxEventType eventType)
	{
		this.target = target;
		this.eventType = eventType;
	}

	/**
	 * @return the target
	 */
	public AjaxRequestTarget getTarget()
	{
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(AjaxRequestTarget target)
	{
		this.target = target;
	}

	/**
	 * @return the eventType
	 */
	public AjaxEventType getEventType()
	{
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(AjaxEventType eventType)
	{
		this.eventType = eventType;
	}
	
}
