/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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

	public BasePage(ResourceModel pageTitle)
	{
		super();
		
		setupPage(pageTitle);
	}
	
	public BasePage(ResourceModel pageTitle, IModel model)
	{
		super(model);
		
		setupPage(pageTitle);
	}	

	private void setupPage(ResourceModel pageTitle)
	{
		add(getMainNavPanel("mainNav"));
		add(new Label("pageTitle", pageTitle));
		
	}
	
	protected Panel getMainNavPanel(String id)
	{
		return getPageConfig().getMainNavPanel(id);
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
	
	protected EhourWebSession getEhourWebSession()
	{
		return EhourWebSession.getSession();
	}
	
	/**
	 * Get this user's config
	 * @return
	 */
	protected EhourConfig getConfig()
	{
		return getEhourWebSession().getEhourConfig();
	}
}
