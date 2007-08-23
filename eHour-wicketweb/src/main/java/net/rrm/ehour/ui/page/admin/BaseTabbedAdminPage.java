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

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.component.CustomAjaxTabbedPanel;
import net.rrm.ehour.ui.model.AdminBackingBean;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 **/

public abstract class BaseTabbedAdminPage extends BaseAdminPage
{
	private	CustomAjaxTabbedPanel		tabbedPanel;
	private AdminBackingBean	addBackingBean;
	private AdminBackingBean	editBackingBean;
	private	ResourceModel		addTabTitle;
	private	ResourceModel		editTabTitle;
	
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
		
		this.addTabTitle = addTabTitle;
		this.editTabTitle = editTabTitle;
		
		addBackingBean = getNewAddBackingBean();
		editBackingBean = getNewEditBackingBean();
		
		setUpTabs();
	}

	/**
	 * Switch tab
	 * @param tab
	 * @param userId
	 */
	protected void switchTabOnAjaxTarget(AjaxRequestTarget target, int tabIndex)
	{
		if (tabIndex == 1)
		{
			addEditTab();
		}
		
		tabbedPanel.setSelectedTab(tabIndex);
		target.addComponent(tabbedPanel);
	}	
	
	/**
	 * Successful save
	 * @param target
	 */
	protected void succesfulSave(AjaxRequestTarget target)
	{
		getAddBackingBean().setServerMessage(getLocalizer().getString("dataSaved", this));
		addAddTab();
		tabbedPanel.setSelectedTab(0);
		
		target.addComponent(tabbedPanel);
	}

	/**
	 * Failed save
	 * @param target
	 */
	protected void failedSave(AdminBackingBean backingBean, AjaxRequestTarget target)
	{
		backingBean.setServerMessage(getLocalizer().getString("saveError", this));
		target.addComponent(tabbedPanel);
	}
	
	/**
	 * Setup tabs
	 */
	private void setUpTabs()
	{
		List<AbstractTab>	tabs = new ArrayList<AbstractTab>(2);
		
		tabbedPanel = new CustomAjaxTabbedPanel("tabs", tabs)
		{
			@Override
			protected void preProcessTabSwitch(int index)
			{
				// if "Add user" is clicked again, reset the backing bean as it's
				// only way out if for some reason the save went wrong and the page is stuck on
				// an error
				if (getSelectedTab() == index && index == 0)
				{
					addBackingBean = getNewAddBackingBean();
				}
				
				// reset server messages
				addBackingBean.setServerMessage(null);
				editBackingBean.setServerMessage(null);
			}
		};

		addAddTab();
		addNoUserTab();
		
		add(tabbedPanel);
	}	
	
	/**
	 * Get the backing bean for the add panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewAddBackingBean();
	
	/**
	 * Get the backing bean for the edit panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewEditBackingBean();
	
	/**
	 * Get the panel for the add tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getAddPanel(String panelId);
	
	/**
	 * Get the panel for the edit tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getEditPanel(String panelId);
	
	/**
	 * Get the panel for the no-selection-made-yet tab (edit tab but no entity selected yet)
	 * @param panelId
	 * @return
	 */
	protected Panel getNoSelectionPanel(String panelId)
	{
		return new NoEntrySelected(panelId);
	}
	
	/**
	 * Add add tab at position 0
	 */
	private void addAddTab()
	{
		tabbedPanel.removeTab(0);
		
		AbstractTab addTab = new AbstractTab(addTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getAddPanel(panelId);
			}
		};

		tabbedPanel.getTabs().add(0, addTab);	
	}	
	
	/**
	 * Add edit tab at position 1
	 */
	private void addEditTab()
	{
		tabbedPanel.removeTab(1);
		
		AbstractTab editTab = new AbstractTab(editTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getEditPanel(panelId);
			}
		};

		tabbedPanel.getTabs().add(1, editTab);		
	}
	
	/**
	 * Add no user selected tab at position 1
	 */
	private void addNoUserTab()
	{
		tabbedPanel.removeTab(1);
		
		AbstractTab editTab = new AbstractTab(editTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getNoSelectionPanel(panelId);
			}
		};

		tabbedPanel.getTabs().add(1, editTab);		
	}	

	/**
	 * 
	 * @return
	 */
	public AdminBackingBean getAddBackingBean()
	{
		return addBackingBean;
	}

	/**
	 * 
	 * @return
	 */
	public AdminBackingBean getEditBackingBean()
	{
		return editBackingBean;
	}

	/**
	 * 
	 * @param editBackingBean
	 */
	public void setEditBackingBean(AdminBackingBean editBackingBean)
	{
		this.editBackingBean = editBackingBean;
	}	
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private class NoEntrySelected extends Panel
	{
		private static final long serialVersionUID = -4318947090257979895L;

		public NoEntrySelected(String id)
		{
			super(id);
			
			GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
			add(greyBorder);

			greyBorder.add(new Label("noUser", new ResourceModel("admin.noEditEntrySelected")));
		}
	}		
}
