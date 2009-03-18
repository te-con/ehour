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

package net.rrm.ehour.ui.common.ajax;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Transfer object for ajax events
 **/

public class AjaxEvent implements Serializable
{
	private static final long serialVersionUID = -8723330496152721044L;
	private AjaxEventType	eventType;
	
	/**
	 * 
	 * @param target
	 * @param eventType
	 */
	public AjaxEvent(AjaxEventType eventType)
	{
		this.eventType = eventType;
	}

	/**
	 * @return the target
	 */
	public AjaxRequestTarget getTarget()
	{
		return AjaxRequestTarget.get();
	}

	/**
	 * @return the eventType
	 */
	public AjaxEventType getEventType()
	{
		return eventType;
	}
}
