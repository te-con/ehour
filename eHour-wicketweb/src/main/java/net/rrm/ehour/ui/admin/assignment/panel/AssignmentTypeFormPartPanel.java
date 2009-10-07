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

package net.rrm.ehour.ui.admin.assignment.panel;

import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.admin.assignment.component.EditDatePanel;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.DynamicAttributeModifier;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.model.FloatModel;
import net.rrm.ehour.ui.common.renderers.ProjectAssignmentTypeRenderer;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.validator.DateOverlapValidator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.NumberValidator;

/**
 * Assignment type part of form 
 **/

public class AssignmentTypeFormPartPanel extends Panel
{
	private static final long serialVersionUID = 1L;
	private Component[] notifiableComponents;
	
	
	@SpringBean
	private ProjectAssignmentService	projectAssignmentService;
	
	public AssignmentTypeFormPartPanel(String id, IModel model, Form form)
	{
		super(id, model);
		
		addAssignmentType(model);
		addDates(form, model);
	}

	/**
	 * 
	 * @return
	 */
	public Component[] getNotifiableComponents()
	{
		return notifiableComponents;
	}
	
	/**
	 * Add assignment types and options
	 * @param form
	 * @param assignmenTypes
	 */
	private void addAssignmentType(IModel model)
	{
		List<ProjectAssignmentType> assignmentTypes = projectAssignmentService.getProjectAssignmentTypes();		

		EhourConfig config = ((EhourWebSession)getSession()).getEhourConfig();
		
		final PropertyModel	showAllottedHoursModel = new PropertyModel(model, "showAllottedHours");
		final PropertyModel	showOverrunHoursModel = new PropertyModel(model, "showOverrunHours");
		
		// assignment type
		final DropDownChoice assignmentTypeChoice = new DropDownChoice("projectAssignment.assignmentType", assignmentTypes, new ProjectAssignmentTypeRenderer());
		assignmentTypeChoice.setRequired(true);
		assignmentTypeChoice.setNullValid(false);
		assignmentTypeChoice.setLabel(new ResourceModel("admin.assignment.type"));
		assignmentTypeChoice.add(new ValidatingFormComponentAjaxBehavior());
		add(assignmentTypeChoice);
		add(new AjaxFormComponentFeedbackIndicator("typeValidationError", assignmentTypeChoice));
		
		// allotted hours 
		final TextField allottedHours = new RequiredTextField("projectAssignment.allottedHours",
												new FloatModel(new PropertyModel(model, "projectAssignment.allottedHours"), config, null));
		allottedHours.setType(float.class);
		allottedHours.add(new ValidatingFormComponentAjaxBehavior());
		allottedHours.add(NumberValidator.POSITIVE);
		allottedHours.setOutputMarkupId(true);
		allottedHours.setLabel(new ResourceModel("admin.assignment.timeAllotted"));
		allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
		
		// allotted hours row
		final WebMarkupContainer allottedRow = new WebMarkupContainer("allottedRow");
		allottedRow.setOutputMarkupId(true);
		allottedRow.add(allottedHours);
		allottedRow.add(new AjaxFormComponentFeedbackIndicator("allottedHoursValidationError", allottedHours));
		allottedRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		add(allottedRow);
		
		// overrun hours 
		final TextField overrunHours = new RequiredTextField("projectAssignment.allowedOverrun",
											new FloatModel(new PropertyModel(model, "projectAssignment.allowedOverrun"), config, null));
		overrunHours.setType(float.class);
		overrunHours.add(new ValidatingFormComponentAjaxBehavior());
		overrunHours.add(NumberValidator.POSITIVE);
		overrunHours.setOutputMarkupId(true);
		overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
		overrunHours.setLabel(new ResourceModel("admin.assignment.allowedOverrun"));
		
		// overrun hours row
		final WebMarkupContainer overrunRow = new WebMarkupContainer("overrunRow");
		overrunRow.setOutputMarkupId(true);
		overrunRow.add(overrunHours);
		overrunRow.add(new AjaxFormComponentFeedbackIndicator("overrunHoursValidationError", overrunHours));
		overrunRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showOverrunHoursModel));
		add(overrunRow);
		
		// notify PM when possible
		CheckBox notifyPm = new CheckBox("projectAssignment.notifyPm");
		notifyPm.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled")));
		notifyPm.setOutputMarkupId(true);
		
		Label notifyDisabled = new Label("notifyDisabled", new ResourceModel("admin.assignment.cantNotify"));
		notifyDisabled.add(new DynamicAttributeModifier("style", true, 
														new Model("display: none;"), new PropertyModel(model, "notifyPmEnabled"), true));
		notifyDisabled.setOutputMarkupId(true);
		
		// notify PM row
		final WebMarkupContainer notifyPmRow = new WebMarkupContainer("notifyPmRow");
		notifyPmRow.setOutputMarkupId(true);
		notifyPmRow.add(notifyPm);
		notifyPmRow.add(notifyDisabled);
		notifyPmRow.add(new DynamicAttributeModifier("style", true, new Model("display: none;"), showAllottedHoursModel));
		add(notifyPmRow);
		
		assignmentTypeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			private static final long serialVersionUID = 1L;

			protected void onUpdate(AjaxRequestTarget target)
            {
				// to disable the required validation
				allottedHours.setEnabled(((Boolean)showAllottedHoursModel.getObject()).booleanValue());
				overrunHours.setEnabled(((Boolean)showOverrunHoursModel.getObject()).booleanValue());
				target.addComponent(allottedHours);
				target.addComponent(overrunHours);
				
				// show/hide rows dependent on the assignment type selected
				target.addComponent(allottedRow);
				target.addComponent(overrunRow);
				target.addComponent(notifyPmRow);
            }
        });	
		
		notifiableComponents = new Component[]{notifyPm, notifyDisabled};
	}
	
	/**
	 * Add start & end dates
	 * @param form
	 * @param model
	 */
	private void addDates(Form form, final IModel model)
	{
		EditDatePanel dateStart = new EditDatePanel("dateStart", new PropertyModel(model, "projectAssignment.dateStart"), new PropertyModel(model, "infiniteStartDate"));
		EditDatePanel dateEnd = new EditDatePanel("dateEnd", new PropertyModel(model, "projectAssignment.dateEnd"), new PropertyModel(model, "infiniteEndDate"));

		add(dateStart);
		add(dateEnd);
		
		form.add(new DateOverlapValidator(dateStart.getDateInputFormComponent(), dateEnd.getDateInputFormComponent()));
	}	
}
