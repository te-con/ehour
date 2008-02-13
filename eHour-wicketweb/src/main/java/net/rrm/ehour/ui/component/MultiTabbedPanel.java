/**
 * Created on Feb 13, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.component;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Multi tabbed
 **/

public abstract class MultiTabbedPanel extends AjaxTabbedPanel
{
	public MultiTabbedPanel(String id)
	{
		super(id, new ArrayList<AbstractTab>());
	}

	/**
	 * Set selected tab based on id
	 * @param title
	 */
	public void setSelectedTabOnId(String id)
	{
		int i = 0;
		
		for (Object tabObj : getTabs())
		{
			if (tabObj instanceof AbstractIdTab)
			{
				AbstractIdTab idTab = (AbstractIdTab)tabObj;
				
				if (idTab.getId().equals(id))
				{
					setSelectedTab(i);
					break;
				}
			}
			
			i++;
		}
	}	
	
	/**
	 * Is the with the id already added
	 * @param id
	 * @return
	 */
	public boolean isTabIdAdded(AbstractIdTab.TabId id)
	{
		for (Object tabObj : getTabs())
		{
			if (tabObj instanceof AbstractIdTab)
			{
				AbstractIdTab idTab = (AbstractIdTab)tabObj;
				
				if (idTab.getId().equals(id))
				{
					return true;
				}
			}
		}
		
		return false;
	}	
	
	
	/**
	 * Removes tab from specified position
	 * @param index
	 */
	public void removeTab(int index)
	{
		if (getTabs().size() >= index + 1)
		{
			getTabs().remove(index);;
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel#newLink(java.lang.String, int)
	 */
	@Override
	protected WebMarkupContainer newLink(String linkId, final int index)
	{
		return new AjaxFallbackLink(linkId)
		{

			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target)
			{
				preProcessTabSwitch(index);
				
				setSelectedTab(index);
				
				if (target != null)
				{
					target.addComponent(MultiTabbedPanel.this);
				}
				onAjaxUpdate(target);
			}
		};
	}
	
	/**
	 * 
	 * @param index
	 */
	protected void preProcessTabSwitch(int index)
	{
		
	}
}
