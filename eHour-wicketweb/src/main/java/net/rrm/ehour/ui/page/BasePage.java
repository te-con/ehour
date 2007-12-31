/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.page;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.AjaxEvent;
import net.rrm.ehour.ui.panel.nav.MainNavPanel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base layout of all pages, adds header panel
 **/

public abstract class BasePage extends WebPage implements AjaxAwareContainer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7090746921483608658L;

	/**
	 * 
	 * @param title
	 * @param model
	 */
	public BasePage(String pageTitle, IModel model)
	{
		super(model);

		add(new MainNavPanel("mainNav"));
		add(new Label("pageTitle", pageTitle));
	}
	
	/**
	 * 
	 * @param title
	 * @param model
	 */
	public BasePage(ResourceModel pageTitle, IModel model)
	{
		super(model);

		add(new MainNavPanel("mainNav"));
		add(new Label("pageTitle", pageTitle));
	}	
	
	/**
	 * Get config
	 * @return
	 */
	protected EhourConfig getEhourConfig()
	{
		EhourWebSession session = (EhourWebSession)getSession();
		return session.getEhourConfig();
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
	
	/**
	 * 
	 * @return
	 */
	protected EhourWebSession getEhourWebSession()
	{
		return ((EhourWebSession)this.getSession());
	}
	
	/**
	 * Get this user's config
	 * @return
	 */
	protected EhourConfig getConfig()
	{
		return EhourWebSession.getSession().getEhourConfig();
	}
}
