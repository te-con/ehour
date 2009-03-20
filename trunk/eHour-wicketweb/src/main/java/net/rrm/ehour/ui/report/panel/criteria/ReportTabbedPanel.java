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

package net.rrm.ehour.ui.report.panel.criteria;

import java.util.List;

import net.rrm.ehour.ui.common.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.component.OpenFlashChart;
import net.rrm.ehour.ui.common.model.KeyResourceModel;
import net.rrm.ehour.ui.common.util.CommonWebUtil;

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
	public ReportTabbedPanel(final String id, final List<AbstractTab> tabs)
	{
		super(id, tabs);
		
		setOutputMarkupId(true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.extensions.ajax.markup.html.tabs.AjaxTabbedPanel#newLink(java.lang.String, int)
	 */
	@Override
	protected WebMarkupContainer newLink(final String linkId, final int index)
	{
		return new AjaxFallbackLink(linkId)
		{
			private static final long serialVersionUID = 1L;

			public void onClick(final AjaxRequestTarget target)
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
	public void addTab(final ITab newTab)
	{
		final List<ITab> 	tabList = getTabs();
		boolean		tabAdded = false;
		final String		key = ((KeyResourceModel)newTab.getTitle()).getKey();
		int			tabIndex = 0;
		
		for (final ITab tab : tabList)
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
	
	public void initCharts()
	{
		initCharts(AjaxRequestTarget.get());
	}
	
	
	@Override
	protected void onAjaxUpdate(AjaxRequestTarget target)
	{
		initCharts(target);
	}

	/**
	 * @param target
	 */
	private void initCharts(AjaxRequestTarget target)
	{
		target.registerRespondListener(new OpenFlashChartInitializeListener());
	}	
	
	/**
	 * Appends the javascript needed to initialize the open flash charts to the ajaxRequestTarget.
	 *
	 */
	private class OpenFlashChartInitializeListener implements AjaxRequestTarget.ITargetRespondListener
	{
		public void onTargetRespond(AjaxRequestTarget target)
		{
			List<OpenFlashChart> charts = CommonWebUtil.findComponent(ReportTabbedPanel.this, OpenFlashChart.class);

			if (AjaxRequestTarget.get() != null)
			{
				for (OpenFlashChart flashChart : charts)
				{
					AjaxRequestTarget.get().appendJavascript(flashChart.getJavascript());
				}
			}
		}
	}
}
