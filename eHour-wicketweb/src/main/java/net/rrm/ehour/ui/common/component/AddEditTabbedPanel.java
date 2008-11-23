/**
 * Created on Aug 19, 2007
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

package net.rrm.ehour.ui.common.component;

import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.NoEntrySelectedPanel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * AjaxTabbedPanel that passes the index to a pre process method
 **/
@SuppressWarnings({"unchecked", "serial"})
public abstract class AddEditTabbedPanel extends MultiTabbedPanel
{
	private static final long serialVersionUID = -2437819961082840272L;

	public static final int TABPOS_ADD = 0;
	public static final int TABPOS_EDIT = 1;
	
	private AdminBackingBean	addBackingBean;
	private AdminBackingBean	editBackingBean;
	private	ResourceModel		addTabTitle;
	private	ResourceModel		editTabTitle;
	private ResourceModel 		noEntrySelectedText;
	
	/**
	 * 
	 * @param id
	 * @param tabs
	 */
	public AddEditTabbedPanel(String id, 
								ResourceModel addTabTitle, ResourceModel editTabTitle, 
								ResourceModel noEntrySelectedText)
	{
		super(id);
		
		this.addTabTitle = addTabTitle;
		this.editTabTitle = editTabTitle;
		this.noEntrySelectedText = noEntrySelectedText;
		
		addBackingBean = getNewAddBackingBean();
		editBackingBean = getNewEditBackingBean();
		
		setUpTabs();
	}
	
	/**
	 * Successful save
	 * @param target
	 */
	public void succesfulSave(AjaxRequestTarget target)
	{
		addBackingBean = getNewAddBackingBean();
		addBackingBean.setServerMessage(getLocalizer().getString("general.dataSaved", this));
		addAddTab();
		setSelectedTab(TABPOS_ADD);
		
		target.addComponent(this);
	}

	/**
	 * Failed save
	 * @param target
	 */
	public void failedSave(AdminBackingBean backingBean, AjaxRequestTarget target)
	{
		backingBean.setServerMessage(getLocalizer().getString("general.saveError", this));
		target.addComponent(this);
	}	
	
	
	/**
	 * Setup tabs
	 */
	private void setUpTabs()
	{
		addAddTab();
		addNoUserTab();
	}	

	/**
	 * Add tab on tab index in panel. Removes any tab at the tabIndex first
	 * @param tab
	 * @param tabIndex
	 */
	public void addTab(AbstractTab tab, int tabIndex)
	{
		removeTab(tabIndex);
		
		getTabs().add(tabIndex, tab);
	}
	
	/**
	 * Add add tab at position 0
	 */
	private void addAddTab()
	{
		removeTab(TABPOS_ADD);
		
		AbstractTab addTab = new AbstractTab(addTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getAddPanel(panelId);
			}
		};

		getTabs().add(TABPOS_ADD, addTab);	
	}	
	
	/**
	 * Add tab at the end of the list
	 * @param tab
	 */
	public int addTab(ITab tab)
	{
		getTabs().add(tab);
		
		return getTabs().size() - 1;
	}
	
	
	/**
	 * Get the panel for the add tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getAddPanel(String panelId);
	
	/**
	 * Add no user selected tab at position 1
	 */
	protected void addNoUserTab()
	{
		removeTab(TABPOS_EDIT);
		
		AbstractTab editTab = new AbstractTab(editTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getNoSelectionPanel(panelId);
			}
		};

		getTabs().add(TABPOS_EDIT, editTab);		
	}	
	
	/**
	 * Get the panel for the no-selection-made-yet tab (edit tab but no entity selected yet)
	 * @param panelId
	 * @return
	 */
	protected Panel getNoSelectionPanel(String panelId)
	{
		return new NoEntrySelectedPanel(panelId, false, noEntrySelectedText);
	}	

	
	/**
	 * 
	 * @param target
	 * @param index
	 */
	@Override
	protected void preProcessTabSwitch(int index)
	{
		// if "Add" tab is clicked again, reset the backing bean as it's the
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

	
	/**
	 * Switch tab
	 * @param tab
	 * @param userId
	 */
	public void switchTabOnAjaxTarget(AjaxRequestTarget target, int tabIndex)
	{
		if (tabIndex == TABPOS_EDIT)
		{
			addEditTab();
		}
		
		setSelectedTab(tabIndex);
		target.addComponent(this);
	}

	/**
	 * Add edit tab at position 1
	 */
	private void addEditTab()
	{
		removeTab(TABPOS_EDIT);
		
		AbstractTab editTab = new AbstractTab(editTabTitle)
		{
			@Override
			public Panel getPanel(String panelId)
			{
				return getEditPanel(panelId);
			}
		};

		getTabs().add(TABPOS_EDIT, editTab);		
	}	
	
	/**
	 * Get the backing bean for the add panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewAddBackingBean();
	
	
	/**
	 * Get the panel for the edit tab
	 * @param panelId
	 * @return
	 */
	protected abstract Panel getEditPanel(String panelId);	
	
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
	 * Get the backing bean for the edit panel
	 * @return
	 */
	protected abstract AdminBackingBean getNewEditBackingBean();

	/**
	 * @param editBackingBean the editBackingBean to set
	 */
	public void setEditBackingBean(AdminBackingBean editBackingBean)
	{
		this.editBackingBean = editBackingBean;
	}
}


