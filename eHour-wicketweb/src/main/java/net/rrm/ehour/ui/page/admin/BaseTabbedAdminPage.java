/**
 * Created on Aug 19, 2007
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

package net.rrm.ehour.ui.page.admin;

import net.rrm.ehour.ui.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 * TODO need refactor
 **/

@SuppressWarnings({"unchecked", "serial"})
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
								ResourceModel editTabTitle)
	{
		super(pageTitle, null);
		
		tabbedPanel = new AddEditTabbedPanel("tabs", addTabTitle, editTabTitle)
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
	
}
