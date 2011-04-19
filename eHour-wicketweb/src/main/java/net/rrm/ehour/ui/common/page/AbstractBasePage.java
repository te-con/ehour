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
import net.rrm.ehour.ui.common.component.header.HeaderPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventListener;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base layout of all pages, adds header panel
 **/

public abstract class AbstractBasePage<T> extends WebPage implements AjaxEventListener
{
	private static final long serialVersionUID = 7090746921483608658L;

	public AbstractBasePage(ResourceModel pageTitle)
	{
		super();
		
		setupPage(pageTitle);
	}
	
	public AbstractBasePage(ResourceModel pageTitle, IModel<T> model)
	{
		super(model);
		
		setupPage(pageTitle);
	}	

	private void setupPage(ResourceModel pageTitle)
	{
		add(new HeaderPanel("mainNav"));
		add(new Label("pageTitle", pageTitle));
		add(createDebugBar("debug"));
	}
	
	private Component createDebugBar(String id)
	{
		String type = getApplication().getConfigurationType();
		
		if (type.equalsIgnoreCase(Application.DEVELOPMENT))
		{
			return new DebugBar(id);
		}
		else
		{
			WebMarkupContainer container = new WebMarkupContainer(id);
			container.setVisible(false);
			return container;
		}
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxAwareContainer#publishAjaxEvent(net.rrm.ehour.persistence.persistence.ui.common.ajax.AjaxEvent)
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
