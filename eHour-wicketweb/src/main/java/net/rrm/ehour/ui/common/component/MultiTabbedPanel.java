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

package net.rrm.ehour.ui.common.component;

import java.util.ArrayList;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Multi tabbed
 **/

public class MultiTabbedPanel extends AjaxTabbedPanel
{
	private static final long serialVersionUID = -6237966897046476493L;

	public MultiTabbedPanel(String id)
	{
		super(id, new ArrayList<AbstractTab>());
	}

	/**
	 * Set selected tab based on id
	 * @param title
	 */
	public void setSelectedTabOnId(AbstractIdTab.TabId id)
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
	 * 
	 * @param tab
	 */
	@SuppressWarnings("unchecked")
	public void addOrUpdateTab(AbstractIdTab tab)
	{
		int idx = getIndexForTabId(tab.getId());
		
		if (idx == -1)
		{
			getTabs().add(tab);
		}
		else
		{
			getTabs().set(idx, tab);
		}
	}
	
	/**
	 * Is the with the id already added
	 * @param id
	 * @return
	 */
	public int getIndexForTabId(AbstractIdTab.TabId id)
	{
		int index = 0;
		
		for (Object tabObj : getTabs())
		{
			if (tabObj instanceof AbstractIdTab)
			{
				AbstractIdTab idTab = (AbstractIdTab)tabObj;
				
				if (idTab.getId().equals(id))
				{
					return index;
				}
			}
			
			index++;
		}
		
		return -1;
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
