/**
 * Created on Jul 20, 2007
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

package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.panel.AbstractAjaxPanel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * Selector with autocompletion filter 
 **/

public class EntrySelectorPanel extends AbstractAjaxPanel
{
	public final static int ENTRYSELECTOR_WIDTH = 250;
	
	private	StringResourceModel	defaultFilterText;
	private	IModel	checkBoxPrefixText;
	private	boolean	includeFilter = false;
	private	boolean	includeCheckboxToggle = false;
	private GreyBlueRoundedBorder blueBorder;
	private static final long serialVersionUID = -7928428437664050056L;

	/**
	 * EntrySelectorPanel without filter or checkbox toggle
	 * @param id
	 * @param title
	 * @param defaultFilterInputText
	 * @param itemList
	 */
	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder)
	{
		this(id, itemListHolder, null);
	}
	
	/**
	 * EntrySelectorPanel with filter but without checkbox toggle
	 * @param id
	 * @param title
	 * @param itemList
	 * @param defaultFilterText
	 */
	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder, StringResourceModel defaultFilterText)
	{
		this(id, itemListHolder, defaultFilterText, null);
	}

	/**
	 * Fully featured EntrySelectorPanel
	 * @param id
	 * @param title
	 * @param itemList
	 * @param defaultFilterText
	 * @param checkboxPrefix
	 */
	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder, StringResourceModel defaultFilter, IModel checkboxPrefix)
	{
		super(id);

		if (defaultFilter != null)
		{
			this.defaultFilterText = defaultFilter;
			includeFilter = true;
		}
		
		if (checkboxPrefix != null)
		{
			this.checkBoxPrefixText = checkboxPrefix;
			includeCheckboxToggle = true;
		}

		setUpPanel(itemListHolder);
	}	

	/**
	 * 
	 * @param target
	 */
	public void refreshList(AjaxRequestTarget target)
	{
		target.addComponent(blueBorder);
	}

	/**
	 * Setup page
	 */
	protected void setUpPanel(WebMarkupContainer itemListHolder)
	{
		WebMarkupContainer selectorFrame = new WebMarkupContainer("entrySelectorFrame");
		
		blueBorder = new GreyBlueRoundedBorder("blueBorder");
		blueBorder.setOutputMarkupId(true);
		selectorFrame.add(blueBorder);
		
		selectorFrame.add(getFilterForm());
		
		add(selectorFrame);
		
		blueBorder.add(itemListHolder);
	}
	
	/**
	 * Setup the filter form
	 * @param parent
	 */
	protected Form getFilterForm()
	{
		final EntrySelectorFilter filter = new EntrySelectorFilter(defaultFilterText);
		filter.setOnId(this.getId());
		filter.setActivateToggle(getEhourWebSession().getHideInactiveSelections());
		
		Form	filterForm = new Form("filterForm");
		
		WebMarkupContainer filterInputContainer = new WebMarkupContainer("filterInputContainer");
		add(filterInputContainer);
		filterInputContainer.setVisible(this.includeFilter);
		filterForm.add(filterInputContainer);
		
		final TextField	filterInputField = new TextField("filterInput", new PropertyModel(filter, "filterInput"));
		filterInputField.setVisible(this.includeFilter);
		filterInputContainer.add(filterInputField);
		
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("filterToggle", new PropertyModel(filter, "activateToggle"))
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
            	getEhourWebSession().setHideInactiveSelections(filter.isActivateToggle());

            	callbackAfterFilter(target, filter);
			}
		};
		
		deactivateBox.setVisible(includeCheckboxToggle);
		filterForm.add(deactivateBox);
		
		Label filterToggleText = new Label("filterToggleText", checkBoxPrefixText);
		filterForm.add(filterToggleText);

		// if filter included, attach onchange ajax behaviour otherwise hide it
		if (includeFilter)
		{
			filterInputField.add(new AttributeModifier("onclick", true, new AbstractReadOnlyModel()
	        {
				private static final long serialVersionUID = -1;
	            public Object getObject()
	            {
	                return "this.style.color='#233e55';if (this.value == '" + defaultFilterText.getString() + "') { this.value='';}";
	            }
	        }));
			
			OnChangeAjaxBehavior onChangeAjaxBehavior = new OnChangeAjaxBehavior()
	        {
				private static final long serialVersionUID = -1;
				
	            @Override
	            protected void onUpdate(AjaxRequestTarget target)
	            {
	            	callbackAfterFilter(target, filter);
	            }
	        };
	        
	        onChangeAjaxBehavior.setThrottleDelay(Duration.milliseconds(500));
	        
	        filterInputField.add(onChangeAjaxBehavior);
		}
		else
		{
			// hide everything 
			filterInputField.setVisible(false);
		}
		
		filterForm.setVisible(includeFilter || includeCheckboxToggle);
		
		return filterForm;
	}
	
	/**
	 * Call back
	 * @param target
	 * @param filter
	 */
	protected void callbackAfterFilter(AjaxRequestTarget target, EntrySelectorFilter filter)
	{
		PayloadAjaxEvent<EntrySelectorFilter> payloadEvent = new PayloadAjaxEvent<EntrySelectorFilter>(EntrySelectorAjaxEventType.FILTER_CHANGE,
																										filter);
		AjaxUtil.publishAjaxEvent(this, payloadEvent);
		
    	target.addComponent(blueBorder);
	}
}
