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

package net.rrm.ehour.ui.common.page;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.EhourWebApplication;
import net.rrm.ehour.ui.common.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.config.PageConfig;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base layout of all pages, adds header panel
 **/

public abstract class BasePage extends WebPage implements AjaxAwareContainer
{
	private static final long serialVersionUID = 7090746921483608658L;


	/**
	 * 
	 * @param pageTitle
	 */
	public BasePage(ResourceModel pageTitle)
	{
		super();
		
		setupPage(pageTitle);
	}
	
	/**
	 * 
	 * @param title
	 * @param model
	 */
	public BasePage(ResourceModel pageTitle, IModel model)
	{
		super(model);
		
		setupPage(pageTitle);
	}	

	/**
	 * 
	 * @param pageTitle
	 */
	private void setupPage(ResourceModel pageTitle)
	{
		add(getMainNavPanel("mainNav"));
		add(new Label("pageTitle", pageTitle));
		
	}
	
	/**
	 * Get main navigation panel
	 * @param id
	 * @return
	 */
	protected Panel getMainNavPanel(String id)
	{
		return getPageConfig().getMainNavPanel(id);
	}
	
	/**
	 * Get ehour application config
	 * @return
	 */
	protected EhourConfig getEhourConfig()
	{
		EhourWebSession session = (EhourWebSession)getSession();
		return session.getEhourConfig();
	}
	
	/**
	 * Get page config
	 * @return
	 */
	protected PageConfig getPageConfig()
	{
		return ((EhourWebApplication)getApplication()).getPageConfig();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.ajax.AjaxAwareContainer#publishAjaxEvent(net.rrm.ehour.ui.common.ajax.AjaxEvent)
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
