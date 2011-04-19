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

package net.rrm.ehour.ui.common.panel.entryselector;

import net.rrm.ehour.ui.common.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
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

public class EntrySelectorPanel extends AbstractAjaxPanel<Void>
{
	private	StringResourceModel	defaultFilterText;
	private	IModel<String> checkBoxPrefixText;
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
	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder, StringResourceModel defaultFilter, IModel<String> checkboxPrefix)
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
	protected Form<Void> getFilterForm()
	{
		final EntrySelectorFilter filter = new EntrySelectorFilter(defaultFilterText);
		filter.setOnId(this.getId());
		filter.setActivateToggle(getEhourWebSession().getHideInactiveSelections());
		
		Form<Void> filterForm = new Form<Void>("filterForm");
		
		WebMarkupContainer filterInputContainer = new WebMarkupContainer("filterInputContainer");
		add(filterInputContainer);
		filterInputContainer.setVisible(this.includeFilter);
		filterForm.add(filterInputContainer);
		
		final TextField<String>	filterInputField = new TextField<String>("filterInput", new PropertyModel<String>(filter, "filterInput"));
		filterInputField.setVisible(this.includeFilter);
		filterInputContainer.add(filterInputField);
		
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("filterToggle", new PropertyModel<Boolean>(filter, "activateToggle"))
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
			filterInputField.add(new AttributeModifier("onclick", true, new AbstractReadOnlyModel<String>()
	        {
				private static final long serialVersionUID = -1;
	            public String getObject()
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
		EventPublisher.publishAjaxEvent(this, payloadEvent);
		
    	target.addComponent(blueBorder);
	}
}
