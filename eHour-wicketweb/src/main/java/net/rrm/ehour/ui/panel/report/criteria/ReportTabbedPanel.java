/**
 * Created on Sep 18, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.panel.report.criteria;

import java.util.List;

import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.model.KeyResourceModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Ajax tabbed report panel
 **/

public class ReportTabbedPanel extends AjaxTabbedPanel
{
	private static final long serialVersionUID = 5957279200970383021L;

	/**
	 * Default constructor
	 * @param id
	 * @param tabs
	 */
	public ReportTabbedPanel(String id, List<AbstractTab> tabs)
	{
		super(id, tabs);
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
				setSelectedTab(index);
				if (target != null)
				{
					target.addComponent(ReportTabbedPanel.this);
				}
				onAjaxUpdate(target);
			}
			
			// only diff with overriden method
			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				return new LoadingSpinnerDecorator();
			}			

		};
	}	
	
	/**
	 * Add tab, replacing any tabs that have the same title resource key
	 * @param newTab make sure to use a KeyResourceModel for the title
	 */
	@SuppressWarnings("unchecked")
	public void addTab(ITab newTab)
	{
		List<ITab> 	tabList = getTabs();
		boolean		tabAdded = false;
		String		key = ((KeyResourceModel)newTab.getTitle()).getKey();
		int			tabIndex = 0;
		
		for (ITab tab : tabList)
		{
			if ( ((KeyResourceModel)tab.getTitle()).getKey().equals(key))
			{
				tabList.set(tabIndex, newTab);
				this.setSelectedTab(tabIndex);

				tabAdded = true;
				break;
			}
			
			tabIndex++;
		}
	
		if (!tabAdded)
		{
			getTabs().add(newTab);
			setSelectedTab(tabList.size() - 1);
		}
	}

}
