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

package net.rrm.ehour.ui.report.panel.user.criteria;

import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.LoadAwareButton;
import net.rrm.ehour.ui.common.panel.sidepanel.SidePanel;
import net.rrm.ehour.ui.common.renderers.DomainObjectChoiceRenderer;
import net.rrm.ehour.ui.report.panel.criteria.ReportCriteriaAjaxEventType;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Date range and project selection
 **/

public class UserReportCriteriaPanel extends SidePanel
{
	private static final long serialVersionUID = -6129389795390181179L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public UserReportCriteriaPanel(String id, IModel model)
	{
		this(id, model, true);
	}
	
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public UserReportCriteriaPanel(String id, IModel model, boolean multipleChoice)
	{
		super(id);

		Form form = new Form("criteriaForm");
		
		if (multipleChoice)
		{
			ListMultipleChoice projectDropDown;
			projectDropDown = new ListMultipleChoice("userCriteria.projects", 
												new PropertyModel(model, "availableCriteria.projects"),
												new DomainObjectChoiceRenderer());
			projectDropDown.setMaxRows(3);
			form.add(projectDropDown);
		}
		else
		{
			ListChoice projectDropDown;
			projectDropDown = new ListChoice("userCriteria.projects", 
											new PropertyModel(model, "userCriteria.project"),
											new PropertyModel(model, "availableCriteria.projects"),
											new DomainObjectChoiceRenderer());
			projectDropDown.setNullValid(false);
			projectDropDown.setMaxRows(1);
			projectDropDown.setRequired(true);
			form.add(projectDropDown);
		}
		
		
		addDatePickers(form, model);
		
		addSubmits(form);
		
		this.add(form);
	}	
	
	/**
	 * Add submits
	 * @param form
	 */
	protected void addSubmits(Form form)
	{
		@SuppressWarnings("serial")
		AjaxFallbackButton submitButton = new LoadAwareButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				AjaxUtil.publishAjaxEvent(this, new AjaxEvent(ReportCriteriaAjaxEventType.CRITERIA_UPDATED));
            }
        };
        
        form.add(submitButton);		
	}

	/**
	 * Add date pickers
	 * @param parent
	 * @param reportCriteria
	 */
	private void addDatePickers(WebMarkupContainer parent, IModel model)
	{
		parent.add(addDate("Start", model));
		parent.add(addDate("End", model));
	}
	
	/**
	 * Add date + infinite box
	 * @param idPrefix
	 * @param model
	 * @return
	 */
	private WebMarkupContainer addDate(String idPrefix, IModel model)
	{
		PropertyModel	infiniteDateModel = new PropertyModel(model, "userCriteria.infinite" + idPrefix + "Date");
		
        DateTextField datePicker = new DateTextField("userCriteria.reportRange.date" + idPrefix, new PropertyModel(model,
        													"userCriteria.reportRange.date" + idPrefix), new StyleDateConverter("S-", false));
        datePicker.add(new DatePicker());
		
		// container for hiding
		final WebMarkupContainer dateHider = new WebMarkupContainer(idPrefix.toLowerCase() + "DateHider");
		dateHider.setOutputMarkupId(true);
		
		// the inner hider is just there to hide the <br /> as well
		final WebMarkupContainer	innerDateHider = new WebMarkupContainer("inner" + idPrefix + "DateHider");
		innerDateHider.setOutputMarkupId(true);
		innerDateHider.add(datePicker);
		innerDateHider.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), infiniteDateModel, true));

		dateHider.add(innerDateHider);

		// infinite start date toggle
		@SuppressWarnings("serial")
		AjaxCheckBox infiniteToggle = new AjaxCheckBox("userCriteria.infinite" + idPrefix + "Date")
		{
			@Override
			protected void onUpdate(AjaxRequestTarget target)
			{
				target.addComponent(dateHider);
			}
		};
		
		dateHider.add(infiniteToggle);
		
		return dateHider;
	}
}
