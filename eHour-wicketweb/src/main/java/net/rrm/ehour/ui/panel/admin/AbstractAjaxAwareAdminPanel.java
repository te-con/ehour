/**
 * Created on Aug 31, 2007
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

package net.rrm.ehour.ui.panel.admin;

import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Default impl of awarecontainer + panel which calls the Page to handle the 
 * ajax request
 **/

public abstract class AbstractAjaxAwareAdminPanel extends Panel implements AjaxAwareContainer
{
	public AbstractAjaxAwareAdminPanel(String id)
	{
		super(id);
	}	
	
	public AbstractAjaxAwareAdminPanel(String id, IModel model)
	{
		super(id, model);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, type);
		
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		((AjaxAwareContainer)getPage()).ajaxRequestReceived(target, type, params);
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public void ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		Logger.getLogger(this.getClass()).warn("Uncaught ajax event received. This might be a bug");
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
