/**
 * Created on Mar 10, 2008
 * Author: Thies
 *
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

package net.rrm.ehour.ui.panel;

import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

/**
 * Base panel
 **/

public abstract class BaseAjaxPanel extends BasePanel implements AjaxAwareContainer
{
	/**
	 * 
	 * @param id
	 */
	public BaseAjaxPanel(String id)
	{
		super(id);
	}

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public BaseAjaxPanel(String id, IModel model)
	{
		super(id, model);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		ajaxRequestReceived(target, type, null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		Logger.getLogger(this.getClass()).warn("Uncaught ajax request received. This might be a bug");
	}	

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		Logger.getLogger(this.getClass()).warn("Uncaught ajax event received. This might be a bug");
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#publishAjaxEvent(net.rrm.ehour.ui.ajax.AjaxEvent)
	 */
	public void publishAjaxEvent(AjaxEvent ajaxEvent)
	{
		ajaxEventReceived(ajaxEvent);
	}
}
