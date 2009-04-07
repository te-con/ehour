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

package net.rrm.ehour.ui.admin;

import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.model.AdminBackingBean;

import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 * TODO need refactor, remove those Base methods
 **/

@SuppressWarnings("serial")
public abstract class BaseTabbedAdminPage extends BaseAdminPage
{
	private	AddEditTabbedPanel	tabbedPanel;
	
	/**
	 * 
	 * @param pageTitle
	 * @param addTabTitle
	 * @param editTabTitle
	 */
	public BaseTabbedAdminPage(ResourceModel pageTitle,
								ResourceModel addTabTitle,
								ResourceModel editTabTitle,
								ResourceModel noEntrySelectedText,
								String headerResourceId, String bodyResourceId
								)
	{
		super(pageTitle, null, headerResourceId, bodyResourceId);
		
		
		tabbedPanel = new AddEditTabbedPanel("tabs", addTabTitle, editTabTitle, noEntrySelectedText)
		{
			@Override
			protected Panel getAddPanel(String panelId)
			{
				return getBaseAddPanel(panelId);
			}

			@Override
			protected Panel getEditPanel(String panelId)
			{
				return getBaseEditPanel(panelId);
			}

			@Override
			protected AdminBackingBean getNewAddBackingBean()
			{
				return getNewAddBaseBackingBean();
			}

			@Override
			protected AdminBackingBean getNewEditBackingBean()
			{
				return getNewEditBaseBackingBean();
			}
		};
		
		add(tabbedPanel);
	}
	
	/**
	 * Get the backing bean for the add panel
	 * 
	 * @return
	 */
	protected abstract AdminBackingBean getNewAddBaseBackingBean();
	
	/**
	 * Get the backing bean for the edit panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewEditBaseBackingBean();
	
	/**
	 * Get the panel for the add tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getBaseAddPanel(String panelId);
	
	/**
	 * Get the panel for the edit tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getBaseEditPanel(String panelId);

	/**
	 * @return the tabbedPanel
	 */
	public AddEditTabbedPanel getTabbedPanel()
	{
		return tabbedPanel;
	}
	
	/**
	 * 
	 * @param tab
	 * @return
	 */
	public int addExtraTab(ITab tab)
	{
		return 0;
	}
}
