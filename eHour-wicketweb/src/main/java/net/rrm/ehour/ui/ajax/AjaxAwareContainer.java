/**
 * Created on Aug 30, 2007
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

package net.rrm.ehour.ui.ajax;

import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * Ajax aware container providing a callback method for ajax requests
 **/

public interface AjaxAwareContainer
{
	/**
	 * Ajax request received
	 * @param target
	 * @param type
	 * @d eprecated use ajaxEventReceived
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type);
	
	/**
	 * Ajax request received
	 * @param target
	 * @param type
	 * @param params
	 * @d eprecated use ajaxEventReceived
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params);
	
	/**
	 * Ajax event received
	 * @param ajaxEvent
	 * @return true to proceed with other events or false to stop after this component
	 * @since 0.7.2 (replacement of the ajaxRequestReceived methods)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent);
}
