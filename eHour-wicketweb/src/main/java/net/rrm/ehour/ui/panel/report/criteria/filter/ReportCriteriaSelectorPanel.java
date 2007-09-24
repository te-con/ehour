/**
 * Created on Sep 24, 2007
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

package net.rrm.ehour.ui.panel.report.criteria.filter;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.StringResourceModel;

import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.util.CommonUIStaticData;

/**
 * ReportCriteria selector panel 
 **/

public class ReportCriteriaSelectorPanel extends EntrySelectorPanel
{
	private static final long serialVersionUID = 7116578500509436544L;

	private WebMarkupContainer	itemList;
	private AjaxAwareContainer	callbackHandler;
	
	/**
	 * 
	 * @param id
	 * @param itemListHolder
	 * @param defaultFilter
	 * @param checkboxPrefix
	 */
	
	public ReportCriteriaSelectorPanel(String id, 
										WebMarkupContainer itemList, 
										StringResourceModel defaultFilter, 
										IModel checkboxPrefix,
										AjaxAwareContainer callbackHandler)
	{
		super(id, itemList, defaultFilter, checkboxPrefix);
		
		this.itemList = itemList;
		this.callbackHandler = callbackHandler;
	}
	
	/**
	 * 
	 * @param target
	 */
	@Override
	public void refreshList(AjaxRequestTarget target)
	{
		target.addComponent(itemList);
	}	
	
	/**
	 * Setup panel
	 */
	@Override
	protected void setUpPanel(WebMarkupContainer itemList)
	{
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		add(blueBorder);
		
		blueBorder.add(itemList);
		itemList.setOutputMarkupId(true);
		blueBorder.add(getFilterForm());
	}	
	
	/**
	 * Call back
	 * @param target
	 * @param filter
	 */
	@Override
	protected void callbackAfterFilter(AjaxRequestTarget target, EntrySelectorFilter filter)
	{
		callbackHandler.ajaxRequestReceived(target, 
											CommonUIStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE, 
											filter);
		target.addComponent(itemList);
	}	

}
