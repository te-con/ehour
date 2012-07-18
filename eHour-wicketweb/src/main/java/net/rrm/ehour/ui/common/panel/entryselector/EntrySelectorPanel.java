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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Selector with autocompletion filter 
 **/

public class EntrySelectorPanel extends AbstractAjaxPanel<Void>
{
	private	IModel<String> checkBoxPrefixText;
	private	boolean	includeCheckboxToggle = false;
	private GreyBlueRoundedBorder blueBorder;
	private static final long serialVersionUID = -7928428437664050056L;

	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder)
	{
		this(id, itemListHolder, null);
	}
	
	public EntrySelectorPanel(String id, WebMarkupContainer itemListHolder, IModel<String> checkboxPrefix)
	{
		super(id);

		if (checkboxPrefix != null)
		{
			this.checkBoxPrefixText = checkboxPrefix;
			includeCheckboxToggle = true;
		}

		setUpPanel(itemListHolder);
	}	


	public void refreshList(AjaxRequestTarget target)
	{
		target.add(blueBorder);
	}

	private void setUpPanel(WebMarkupContainer itemListHolder)
	{
		WebMarkupContainer selectorFrame = new WebMarkupContainer("entrySelectorFrame");
		
		blueBorder = new GreyBlueRoundedBorder("blueBorder");
		blueBorder.setOutputMarkupId(true);
		selectorFrame.add(blueBorder);
		
		selectorFrame.add(getFilterForm());
		
		add(selectorFrame);
		
		blueBorder.add(itemListHolder);
	}
	
	private Form<Void> getFilterForm()
	{
		final EntrySelectorFilter filter = new EntrySelectorFilter();
		filter.setOnId(this.getId());
		filter.setActivateToggle(getEhourWebSession().getHideInactiveSelections());
		
		Form<Void> filterForm = new Form<Void>("filterForm");
		
		WebMarkupContainer filterInputContainer = new WebMarkupContainer("filterInputContainer");
		add(filterInputContainer);
		filterForm.add(filterInputContainer);

        WebMarkupContainer listFilter = new WebMarkupContainer("listFilter");
        listFilter.setMarkupId("listFilter");
        listFilter.setOutputMarkupId(true);
        filterInputContainer.add(listFilter);

		
		final AjaxCheckBox	deactivateBox = new AjaxCheckBox("filterToggle", new PropertyModel<Boolean>(filter, "activateToggle"))
		{
			private static final long serialVersionUID = 2585047163449150793L;

			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
            	getEhourWebSession().setHideInactiveSelections(filter.isActivateToggle());

            	callbackAfterFilter(target, filter);

                target.appendJavaScript("filterList();");
			}
		};

        deactivateBox.setVisible(includeCheckboxToggle);
        filterForm.add(deactivateBox);

        Label filterToggleText = new Label("filterToggleText", checkBoxPrefixText);
        filterForm.add(filterToggleText);

		return filterForm;
	}

	private void callbackAfterFilter(AjaxRequestTarget target, EntrySelectorFilter filter)
	{
		PayloadAjaxEvent<EntrySelectorFilter> payloadEvent = new PayloadAjaxEvent<EntrySelectorFilter>(EntrySelectorAjaxEventType.FILTER_CHANGE, filter);
        EventPublisher.publishAjaxEvent(this, payloadEvent);
		
    	target.add(blueBorder);
	}
}
