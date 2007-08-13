/**
 * Created on Jul 20, 2007
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

package net.rrm.ehour.ui.panel.entryselector;

import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.util.CommonStaticData;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * Selector with autocompletion filter 
 **/

public class EntrySelectorPanel extends Panel
{
	private	String	defaultFilterText = "";
	private	String	checkBoxPrefixText = "";
	private	boolean	includeFilter;
	private	boolean	includeCheckboxToggle;
	
	private static final long serialVersionUID = -7928428437664050056L;

	/**
	 * EntrySelectorPanel without filter or checkbox toggle
	 * @param id
	 * @param title
	 * @param defaultFilterInputText
	 * @param itemList
	 */
	public EntrySelectorPanel(String id, ResourceModel title, Fragment itemListHolder)
	{
		this(id, title, itemListHolder, null);
	}

	/**
	 * EntrySelectorPanel with filter but without checkbox toggle
	 * @param id
	 * @param title
	 * @param itemList
	 * @param defaultFilterText
	 */
	public EntrySelectorPanel(String id, ResourceModel title, Fragment itemListHolder, String defaultFilterText)
	{
		this(id, title, itemListHolder, defaultFilterText, null);
	}

	/**
	 * Fully featured EntrySelectorPanel
	 * @param id
	 * @param title
	 * @param itemList
	 * @param defaultFilterText
	 * @param checkboxPrefix
	 */
	public EntrySelectorPanel(String id, ResourceModel title, Fragment itemListHolder, String defaultFilterText, String checkboxPrefix)
	{
		super(id);

		if (defaultFilterText != null)
		{
			this.defaultFilterText = defaultFilterText;
			includeFilter = true;
		}
		
		if (checkBoxPrefixText != null)
		{
			this.checkBoxPrefixText = checkboxPrefix;
			includeCheckboxToggle = true;
		}

		setUpPanel(title, itemListHolder);
	}	


	/**
	 * Setup page
	 */
	private void setUpPanel(ResourceModel title, Fragment itemListHolder)
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", title);
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		blueBorder.setOutputMarkupId(true);
		setUpFilterForm(greyBorder, blueBorder);
		
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		blueBorder.add(itemListHolder);
	}
	
	/**
	 * Setup the filter form
	 * @param parent
	 */
	private void setUpFilterForm(WebMarkupContainer parent, final GreyBlueRoundedBorder border)
	{
		final EntrySelectorFilter filter = new EntrySelectorFilter(defaultFilterText);
		
		Form	filterForm = new Form("filterForm");
		parent.add(filterForm);
		
		final TextField		filterInputField = new TextField("filterInput", new PropertyModel(filter, "filterInput"));
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("filterToggle", new PropertyModel(filter, "activateToggle"))
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE, filter);
            	target.addComponent(border);
			}
		};
		filterForm.add(deactivateBox);
		deactivateBox.setVisible(includeCheckboxToggle);
		
		Label filterToggleText = new Label("filterToggleText", checkBoxPrefixText);
		filterForm.add(filterToggleText);
		filterForm.setVisible(includeCheckboxToggle);

		// if filter included, attach onchange ajax behaviour otherwise hide it
		if (includeFilter)
		{
			filterInputField.add(new AttributeModifier("onclick", true, new AbstractReadOnlyModel()
	        {
	            public Object getObject()
	            {
	                return "this.style.color='#233e55';if (this.value == '" + defaultFilterText + "') { this.value='';}";
	            }
	        }));
			
			OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
	        {
	            @Override
	            protected void onUpdate(AjaxRequestTarget target)
	            {
					((BasePage)getPage()).ajaxRequestReceived(target, CommonStaticData.AJAX_ENTRYSELECTOR_FILTER_CHANGE, filter);
	            	target.addComponent(border);
	            }
	        };
	        
	        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(500));
	        
	        filterInputField.add(onChangeAjaxBehavior);
		}
		else
		{
			filterInputField.setVisible(false);
		}
		filterForm.add(filterInputField);
	}
}
