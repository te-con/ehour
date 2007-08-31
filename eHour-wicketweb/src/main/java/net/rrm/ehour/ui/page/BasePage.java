/**
 * Created on May 8, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.page;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.panel.nav.MainNavPanel;
import net.rrm.ehour.ui.session.EhourWebSession;

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
	
	/**
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type)
	{
		ajaxRequestReceived(target, type, null);
	}
	
	/**
	 * Handle Ajax request
	 * @param target
	 * @param type of ajax req
	 */
	public void ajaxRequestReceived(AjaxRequestTarget target, int type, Object params)
	{
		
	}	
}
